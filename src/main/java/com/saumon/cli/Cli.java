package com.saumon.cli;

import com.saumon.core.repository.GameRepository;


import com.saumon.core.model.Game;

public class Cli {

    public static void start() {
        System.out.println("Welcome to the Game Store CLI!");
        System.out.println("Here are the available games:");
        GameRepository gameRepository = new GameRepository();
        System.out.println(gameRepository.getAllGames());
    }
}
