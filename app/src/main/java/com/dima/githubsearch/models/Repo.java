package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("name")
    private String name;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("owner")
    private User owner;
    @SerializedName("description")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}