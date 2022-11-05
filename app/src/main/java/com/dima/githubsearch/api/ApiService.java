package com.dima.githubsearch.api;

import com.dima.githubsearch.entity.IssuePayload;
import com.dima.githubsearch.entity.Repos;
import com.dima.githubsearch.entity.ReposPayload;
import com.dima.githubsearch.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search/repositories?per_page=100")
    Observable<ReposPayload> searchRepos(
            @Query("q") String query
    );

    @GET("users/{user}")
    Observable<User> getOwner(
            @Path("user") String login
    );

    @GET("users/{login}/repos")
    Observable<List<Repos>> getRepos(
            @Path("login") String login
    );

    @GET("repositories")
    Observable<List<Repos>> getRepos();

    @GET("repos/{user}/{repo}/issues&sort=created&direction=desc")
    Observable<IssuePayload> getIssues(
            @Path("user") String user,
            @Path("repo") String repo
    );

}
