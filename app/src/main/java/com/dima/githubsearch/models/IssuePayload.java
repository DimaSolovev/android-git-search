package com.dima.githubsearch.models;

import java.util.ArrayList;
import java.util.List;

public class IssuePayload {

    private List<Issue> items = new ArrayList<>();

    public List<Issue> getItems() {
        return items;
    }

    public void setItems(List<Issue> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "IssuePayload{" +
                "items=" + items +
                '}';
    }
}
