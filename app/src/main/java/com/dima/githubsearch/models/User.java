package com.dima.githubsearch.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
