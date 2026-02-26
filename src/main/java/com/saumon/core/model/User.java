package com.saumon.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saumon.core.repository.GameRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private Date registrationDate;
    private List<Integer> games;

    public User() {
    }

    public User(int id, String username, String email, String password, Date registrationDate, List<Integer> games) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.games = games;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public List<Game> fetchGames() {
        List<Game> userGames = new ArrayList<>();
        GameRepository gameRepo = new GameRepository();
        for (Integer gameID : games) {
            userGames.add(gameRepo.getGameById(gameID));
        }
        return userGames;
    }

    public List<Integer> getGames() {
        return games;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addGame(Integer gameID) {
        if (!games.contains(gameID)) {
            games.add(gameID);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setGames(List<Integer> games) {
        this.games = games;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", games=" + games +
                '}';
    }
}
