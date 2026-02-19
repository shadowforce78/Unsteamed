package com.saumon.core.model;

import java.lang.reflect.Array;
import java.util.Date;

public class Game {
    private int id;
    private String name;
    private Double price;
    private Array genres;
    private String description;
    private Date releaseDate;
    private String imageUrl;
    private Boolean isMultiplayer;
    private String studioID;
    private Boolean subscription;
    public Game(int id, String name, Double price, Array genres, String description,Date releaseDate,String imageUrl, Boolean isMultiplayer, String studioID, Boolean subscription) {
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

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Array getGenres() {
        return genres;
    }

    public String getDescription() {
        return description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getIsMultiplayer() {
        return isMultiplayer;
    }

    public String getStudioID() {
        return studioID;
    }

    public Boolean getSubscription() {
        return subscription;
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
