package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepoPayload {

    @SerializedName("items")
    private List<Repo> items;

    public RepoPayload(List<Repo> items) {
        this.items = items;
    }

    public List<Repo> getItems() {
        return items;
    }

}
