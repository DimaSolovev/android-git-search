package com.dima.githubsearch.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedrodimoura on 03/08/16.
 */
public class RepoPayload {

    private List<Repo> items = new ArrayList<>();

    public void addItems(List<Repo> repos){
        getItems().addAll(repos);
    }

    public void clearItems(){
        items.clear();
    }

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ReposPayload{" +
                "items=" + items +
                '}';
    }
}
