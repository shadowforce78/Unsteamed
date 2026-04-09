package com.saumon.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saumon.core.repository.GameRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private Date registrationDate;
    private List<Integer> games;
    private Map<Integer, Integer> progression;
    private Map<Integer, Double> playtime;
    private double balance;

    public User() {
        this.games = new ArrayList<>();
        this.progression = new HashMap<>();
        this.playtime = new HashMap<>();
        this.balance = 1000.0;
    }

    public User(int id, String username, String email, String password, Date registrationDate, List<Integer> games, Map<Integer, Integer> progression, Map<Integer, Double> playtime, double balance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.games = games != null ? games : new ArrayList<>();
        this.progression = progression != null ? progression : new HashMap<>();
        this.playtime = playtime != null ? playtime : new HashMap<>();
        this.balance = balance;
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

    public Map<Integer, Integer> getProgression() {
        return progression;
    }

    public void setProgression(Map<Integer, Integer> progression) {
        this.progression = progression;
    }

    public Map<Integer, Double> getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Map<Integer, Double> playtime) {
        this.playtime = playtime;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

    public void updateProgression(Integer gameID, Integer level) {
         if (games.contains(gameID)) {
             if (level < 0) level = 0;
             if (level > 100) level = 100;
             progression.put(gameID, level);
         }
    }

    public void updatePlaytime(Integer gameID, Double hours) {
        if (games.contains(gameID)) {
            if (hours < 0) hours = 0.0;
            playtime.put(gameID, hours);
        }
    }

    public void addBalance(double amount) {
        if(amount > 0) {
            this.balance += amount;
        }
    }

    public boolean deductBalance(double amount) {
        if(this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public boolean refundGame(Game game) {
        Integer gameID = game.getId();
        if (games.contains(gameID)) {
            Double hours = playtime.getOrDefault(gameID, 0.0);
            if (hours < 2.0) {
                games.remove(gameID);
                progression.remove(gameID);
                playtime.remove(gameID);
                addBalance(game.getPrice() != null ? game.getPrice() : 0.0);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", games=" + games +
                ", progression=" + progression +
                ", playtime=" + playtime +
                ", balance=" + balance +
                '}';
    }
}
