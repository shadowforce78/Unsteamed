package com.saumon.cli;

import com.saumon.core.repository.GameRepository;
import com.saumon.core.repository.StudioRepository;
import com.saumon.core.repository.UserRepository;

public class Cli {

    public static void start() {
        GameRepository gameRepository = new GameRepository();
        StudioRepository studioRepository = new StudioRepository();
        UserRepository userRepository = new UserRepository();

        System.out.println(userRepository.getAllUsers());

    }
}
