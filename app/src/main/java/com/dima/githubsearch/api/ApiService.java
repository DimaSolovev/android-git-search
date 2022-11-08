package com.dima.githubsearch.api;

import com.dima.githubsearch.models.Issue;
import com.dima.githubsearch.models.RepoPayload;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search/repositories")
    Observable<RepoPayload> searchRepos(
            @Query("q") String query,
            @Query("page") int page
    );

    @GET("repos/{user}/{repo}/issues?state=all&sort=created&direction=asc")
    Observable<List<Issue>> getIssues(
            @Path("user") String user,
            @Path("repo") String repo
    );



}
