package com.saumon.core.repository;

import com.saumon.core.model.Game;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private List games = new ArrayList<>();

    public GameRepository(){
        games.add(new Game(1, "Game 1", 59.99, null, "Description of Game 1", "http://example.com/game1.jpg", true, "Studio A", false));
        games.add(new Game(2, "Game 2", 39.99, null, "Description of Game 2", "http://example.com/game2.jpg", false, "Studio B", true));
    }

    public List getAllGames() {
        return games;
    }

    public Game getGameById(int id) {
        for (Object game : games) {
            if (((Game) game).getId() == id) {
                return (Game) game;
            }
        }
        return null;
    }
}
