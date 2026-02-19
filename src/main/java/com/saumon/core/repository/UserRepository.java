package com.saumon.core.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saumon.core.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();
    private final String DATA_FILE = "data/users.json";

    public UserRepository() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                users = mapper.readValue(file, new TypeReference<List<User>>() {});
            } else {
                System.out.println("Data file not found: " + DATA_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public void registerUser(User newUser) {
        int newId = users.size() + 1; // Simple ID generation strategy
        newUser.setId(newId);
        users.add(newUser);
        saveUsers();
    }

    public void saveUsers() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append("ID: ").append(user.getId())
              .append(", Username: ").append(user.getUsername())
              .append(", Email: ").append(user.getEmail())
              .append(", Registration Date: ").append(user.getRegistrationDate())
              .append("\n");
        }
        return sb.toString();
    }

}
