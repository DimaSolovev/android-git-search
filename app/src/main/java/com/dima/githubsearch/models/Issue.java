package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("title")
    private String title;
    @SerializedName("state")
    private String state;
    @SerializedName("created_at")
    private String createdAt;

    public Issue(String title, String state, String createdAt) {
        this.title = title;
        this.state = state;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
