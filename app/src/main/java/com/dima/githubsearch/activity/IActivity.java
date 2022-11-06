package com.dima.githubsearch.activity;

import android.app.Activity;

import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.ReposPayload;
import com.dima.githubsearch.models.User;

public interface IActivity {

    void showReposOnUI(ReposPayload reposPayload);

    void showErrorOnUI(Throwable t);

    void showIssueOnUI(IssuePayload issuePayload);

}
