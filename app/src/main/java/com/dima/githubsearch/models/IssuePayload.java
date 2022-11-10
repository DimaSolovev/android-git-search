package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IssuePayload {

    @SerializedName("items")
    private List<Issue> items = new ArrayList<>();

    public List<Issue> getItems() {
        return items;
    }

    public void setItems(List<Issue> items) {
        this.items = items;
    }

}
