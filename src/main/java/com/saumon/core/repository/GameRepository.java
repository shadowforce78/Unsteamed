package com.saumon.core.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saumon.core.model.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private List<Game> games = new ArrayList<>();
    private final String DATA_FILE = "data/games.json";

    public GameRepository(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                games = mapper.readValue(file, new TypeReference<List<Game>>(){});
            } else {
                System.out.println("Data file not found: " + DATA_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Game> getAllGames() {
        return games;
    }

    public Game getGameById(int id) {
        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }
}
