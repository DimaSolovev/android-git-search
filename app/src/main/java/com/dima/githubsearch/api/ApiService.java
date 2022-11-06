package com.dima.githubsearch.api;

import com.dima.githubsearch.entity.Issue;
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
            @Query("q") String query,
            @Query("page") String page
    );

    @GET("repos/{user}/{repo}/issues?state=all&sort=created&direction=asc")
    Observable<List<Issue>> getIssues(
            @Path("user") String user,
            @Path("repo") String repo
    );

}
