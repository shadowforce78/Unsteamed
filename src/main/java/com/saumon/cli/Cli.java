package com.saumon.cli;

import com.saumon.core.repository.GameRepository;


import com.saumon.core.model.Game;
import com.saumon.core.repository.StudioRepository;

public class Cli {

    public static void start() {
        System.out.println("Welcome to the Game Store CLI!");
        System.out.println("Here are the available games:");
        GameRepository gameRepository = new GameRepository();
        StudioRepository studioRepository = new StudioRepository();
        // Get all games made by studio with id 1
        System.out.println(studioRepository.getAllStudios());
        System.out.println(studioRepository.getAllGamesID(1));
        System.out.println(studioRepository.getAllGames(1));

    }
}
