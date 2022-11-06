package com.dima.githubsearch.models;

public class Issue {

    private String title;
    private String state;
    private String createdAt;

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "title='" + title + '\'' +
                ", state='" + state + '\'' +
                ", created_at='" + createdAt + '\'' +
                '}';
    }
}
