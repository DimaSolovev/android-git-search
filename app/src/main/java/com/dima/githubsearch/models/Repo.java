package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Repo implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("owner")
    private User owner;
    @SerializedName("description")
    private String description;

    public Repo(String name, String fullName, User owner, String description) {
        this.name = name;
        this.fullName = fullName;
        this.owner = owner;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public User getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

}