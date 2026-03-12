package com.saumon.cli;

import com.saumon.core.model.Game;
import com.saumon.core.model.User;
import com.saumon.core.repository.GameRepository;
import com.saumon.core.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Cli {
    private static final String SESSION_FILE = ".session";
    private static final UserRepository userRepository = new UserRepository();
    private static final GameRepository gameRepository = new GameRepository();

    public static void start(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        switch (command) {
            case "--c":
                createAccount(args);
                break;
            case "--i":
                login(args);
                break;
            case "--a":
                showLibrary();
                break;
            case "--g":
                showCatalog();
                break;
            case "--b":
                buyGame(args);
                break;
            case "--p":
                updateProgression(args);
                break;
            case "--h":
                printHelp();
                break;
            default:
                System.out.println("Unknown command: " + command);
                printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar app.jar [options]");
        System.out.println("Options:");
        System.out.println("  --c <username> <email> <password>  Create account");
        System.out.println("  --i <email> <password>             Login");
        System.out.println("  --a                                Show library");
        System.out.println("  --g                                Show game catalog");
        System.out.println("  --b <gameId>                       Buy/Add game (use comma for multiple IDs, e.g., --b 1,2,3)");
        System.out.println("  --p <gameId> <level>               Update progression");
        System.out.println("  --h                                Show help");
    }

    private static void createAccount(String[] args) {
        if (args.length < 4) {
            System.out.println("Error: Missing arguments for --c. Usage: --c <username> <email> <password>");
            return;
        }
        String username = args[1];
        String email = args[2];
        String password = args[3];

        if (userRepository.getUserByEmail(email) != null) {
            System.out.println("Error: User with this email already exists.");
            return;
        }

        User newUser = new User(0, username, email, password, new Date(), new ArrayList<>(), new HashMap<>());
        userRepository.registerUser(newUser);
        System.out.println("Account created successfully!");
    }

    private static void login(String[] args) {
        if (args.length < 3) {
            System.out.println("Error: Missing arguments for --i. Usage: --i <email> <password>");
            return;
        }
        String email = args[1];
        String password = args[2];

        User user = userRepository.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE))) {
                writer.write(String.valueOf(user.getId()));
                System.out.println("Login successful. Session saved.");
            } catch (IOException e) {
                System.out.println("Error saving session: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static User getSessionUser() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            System.out.println("No user logged in. Please login using --i.");
            return null;
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(SESSION_FILE))).trim();
            int userId = Integer.parseInt(content);
            User user = userRepository.getUserById(userId);
            if (user == null) {
                System.out.println("Session invalid. User not found.");
            }
            return user;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading session.");
            return null;
        }
    }

    private static void showLibrary() {
        User user = getSessionUser();
        if (user == null) return;

        System.out.println("Library for " + user.getUsername() + ":");
        List<Integer> gameIds = user.getGames();
        if (gameIds.isEmpty()) {
            System.out.println("No games in library.");
        } else {
            for (Integer id : gameIds) {
                Game game = gameRepository.getGameById(id);
                if (game != null) {
                    Integer progression = user.getProgression().get(id);
                    System.out.println("- " + game.getName() + " (ID: " + game.getId() + ")" + (progression != null ? " [Level " + progression + "]" : ""));
                }
            }
        }
    }

    private static  void showCatalog() {
        System.out.println("Available Games:");
        for (Game game : gameRepository.getAllGames()) {
            System.out.println(game.getId() + ". " + game.getName() + " - $" + game.getPrice());
        }
    }

    private static void buyGame(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Missing arguments for --b. Usage: --b <gameId> (or comma-separated list of gameIds ex: --b 1,2,3)");
            return;
        }
        // Split args if there are multiple game IDs provided (1,2,3)
        if (args[1].contains(",")) {
            String[] gameIds = args[1].split(",");
            for (String gameIdStr : gameIds) {
                try {
                    int gameId = Integer.parseInt(gameIdStr.trim());
                    buyGame(new String[]{"--b", String.valueOf(gameId)});
                } catch (NumberFormatException e) {
                    System.out.println("Invalid game ID: " + gameIdStr);
                }
            }
            return;
        }

        User user = getSessionUser();
        if (user == null) return;

        try {
            int gameId = Integer.parseInt(args[1]);
            Game game = gameRepository.getGameById(gameId);
            if (game == null) {
                System.out.println("Game not found.");
                return;
            }

            if (user.getGames().contains(gameId)) {
                System.out.println("You already own this game.");
            } else {
                userRepository.addGameToUser(user, game);
                System.out.println("Successfully purchased " + game.getName() + "!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid game ID.");
        }
    }

    private static void updateProgression(String[] args) {
        if (args.length < 3) {
            System.out.println("Error: Missing arguments for --p. Usage: --p <gameId> <level>");
            return;
        }
        User user = getSessionUser();
        if (user == null) return;

        try {
            int gameId = Integer.parseInt(args[1]);
            int level = Integer.parseInt(args[2]);

            if (!user.getGames().contains(gameId)) {
                System.out.println("You don't own this game.");
                return;
            }

            userRepository.updateUserProgression(user, gameId, level);
            System.out.println("Progression updated for game " + gameId + " to level " + level + ".");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}
