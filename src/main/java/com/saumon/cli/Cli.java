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


        User user1 = userRepository.getUserById(1);
        Game game1 = gameRepository.getGameById(2);

        System.out.println(user1);
        System.out.println(game1);

        System.out.println(user1.getGames());
        System.out.println(game1.getId());
        userRepository.addGameToUser(user1, game1);

    }
}
