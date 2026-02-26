package com.saumon.core.model;

import java.util.List;
import java.util.Date;

public class Game {
    private int id;
    private String name;
    private Double price;
    private List<String> genres;
    private String description;
    private Date releaseDate;
    private String imageUrl;
    private Boolean isMultiplayer;
    private int studioID;
    private Boolean subscription;

    public Game() {
    }

    public Game(int id, String name, Double price, List<String> genres, String description,Date releaseDate,String imageUrl, Boolean isMultiplayer, int studioID, Boolean subscription) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.genres = genres;
        this.description = description;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.isMultiplayer = isMultiplayer;
        this.studioID = studioID;
        this.subscription = subscription;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getGenres() {
        return genres;
    }
    public void addGenres(List<String> genres) {
this.genres.addAll(genres);    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsMultiplayer() {
        return isMultiplayer;
    }
        public void setIsMultiplayer(Boolean isMultiplayer) {
            this.isMultiplayer = isMultiplayer;
        }

    public int getStudioID() {
        return studioID;
    }
    public void setStudioID(int studioID) {
        this.studioID = studioID;
    }

    public Boolean getSubscription() {
        return subscription;
    }
    public void setSubscription(Boolean subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", genres=" + genres +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", imageUrl='" + imageUrl + '\'' +
                ", isMultiplayer=" + isMultiplayer +
                ", studioID='" + studioID + '\'' +
                ", subscription=" + subscription +
                '}';
    }

}
