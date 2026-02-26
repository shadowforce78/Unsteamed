package com.saumon.core.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saumon.core.model.Game;
import com.saumon.core.model.Studio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudioRepository {
    private List<Studio> studios = new ArrayList<>();
    private final String DATA_FILE = "data/studios.json";

    public StudioRepository() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                studios = mapper.readValue(file, new TypeReference<List<Studio>>(){});
            } else {
                System.out.println("Data file not found: " + DATA_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Studio> getAllStudios() {
        return studios;
    }

    public Studio getStudioById(int id) {
        for (Studio studio : studios) {
            if (studio.getId() == id) {
                return studio;
            }
        }
        return null;
    }

    public List<Integer> getAllGamesID(Integer studioID){
        List<Integer> gamesIDs = new ArrayList<>();
        Studio studio = getStudioById(studioID);
        if(studio != null){
            gamesIDs = studio.getGamesID();
        }
        return gamesIDs;
    }

    public int getStudioIDByStudioName(String studioName){
        for (Studio studio : studios) {
            if (studio.getName().equalsIgnoreCase(studioName)) {
                return studio.getId();
            }
        }
        return -1; // Return -1 if no matching studio is found
    }

    public List<Game> getAllGames(Integer studioID) {
        List<Game> games = new ArrayList<>();
        Studio studio = getStudioById(studioID);
        if (studio != null) {
            GameRepository gameRepository = new GameRepository();
            games = studio.getGames(gameRepository.getAllGames());
            return games;
        }
        return null;
    }
}
