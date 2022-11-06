package com.dima.githubsearch.activity;

import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.RepoPayload;

public interface IActivity {

    void showReposOnUI(RepoPayload repoPayload);

    void showErrorOnUI(Throwable t);

    void showIssueOnUI(IssuePayload issuePayload);

}
