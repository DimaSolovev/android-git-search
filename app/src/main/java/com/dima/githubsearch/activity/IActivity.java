package com.dima.githubsearch.activity;

import android.app.Activity;

import com.dima.githubsearch.entity.IssuePayload;
import com.dima.githubsearch.entity.ReposPayload;
import com.dima.githubsearch.entity.User;

public interface IActivity {

    void showReposOnUI(ReposPayload reposPayload);

    void showUserOnUI(User user);

    void showErrorOnUI(Throwable t);

    void showErrorOnUI(int resId);

    void showIssueOnUI(IssuePayload issuePayload);

    Activity getActivityContext();
}
