package com.dima.githubsearch.models;

public class Repo {

    private String name;
    private String fullName;
    private User owner;
    private String description;

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

    @Override
    public String toString() {
        return "Repos{" +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", owner=" + owner +
                ", description='" + description + '\'' +
                '}';
    }
}