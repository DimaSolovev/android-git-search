package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepoPayload {

    @SerializedName("items")
    private List<Repo> items = new ArrayList<>();

    public List<Repo> getItems() {
        return items;
    }

}
