package com.dima.githubsearch.models;

import java.util.ArrayList;
import java.util.List;

public class RepoPayload {

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

}
