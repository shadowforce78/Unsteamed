package com.saumon.core.model;

import java.util.List;

public class Studio {
    private int id;
    private String name;
    private String imageUrl;
    private List<Integer> gamesID;

    public Studio() {
    }

    public Studio(int id, String name, String imageUrl, List<Integer> gamesID) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.gamesID = gamesID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Integer> getGamesID() {
        return gamesID;
    }

    public List<Game> getGames(List<Game> allGames) {
        return allGames.stream()
                .filter(game -> gamesID.contains(game.getId()))
                .toList();
    }

    public String toString() {
        return "{id=" + id + ", name='" + name + "', imageUrl='" + imageUrl + "', games=" + gamesID + "}";
    }
}
