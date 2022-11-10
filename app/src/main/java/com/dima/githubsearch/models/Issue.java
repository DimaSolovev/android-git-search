package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("title")
    private String title;
    @SerializedName("state")
    private String state;
    @SerializedName("created_at")
    private String createdAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
