package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepoPayload {

    @SerializedName("items")
    private List<Repo> items = new ArrayList<>();

    public void addItems(List<Repo> repos) {
        getItems().addAll(repos);
    }

    public void clearItems() {
        items.clear();
    }

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }
}
