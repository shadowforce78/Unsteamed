package com.saumon.cli;

import com.saumon.core.model.Game;
import com.saumon.core.model.User;
import com.saumon.core.repository.GameRepository;
import com.saumon.core.repository.StudioRepository;
import com.saumon.core.repository.UserRepository;

import java.util.Date;
import java.util.List;

public class Cli {

    public static void start() {
        GameRepository gameRepository = new GameRepository();
        StudioRepository studioRepository = new StudioRepository();
        UserRepository userRepository = new UserRepository();

        Game newGame = new Game(1, "The Legend of Zelda: Breath of the Wild", 59.99, List.of("Action-adventure"), "An open-world adventure game set in the kingdom of Hyrule.", new Date(), "https://example.com/botw.jpg", false, studioRepository.getStudioIDByStudioName("Nintendo"), false);
        gameRepository.addGame(newGame);
        User user1 = userRepository.getUserById(1);
        System.out.println(user1);

    }
}
