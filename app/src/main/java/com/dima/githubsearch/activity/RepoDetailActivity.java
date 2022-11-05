package com.dima.githubsearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.IssueAdapter;
import com.dima.githubsearch.entity.IssuePayload;
import com.dima.githubsearch.entity.Repos;
import com.dima.githubsearch.entity.ReposPayload;
import com.dima.githubsearch.entity.User;
import com.dima.githubsearch.presenter.ReposPresenter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class RepoDetailActivity extends AppCompatActivity implements IActivity {

    private Repos repo;
    private ImageView imageView;
    private TextView textViewRepoName;
    private TextView textViewRepoDescription;
    private IssueAdapter issueAdapter;
    private RecyclerView issueRecyclerView;
    private ReposPresenter reposPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        imageView = findViewById(R.id.imageView);
        textViewRepoName = findViewById(R.id.textViewRepoName);
        textViewRepoDescription = findViewById(R.id.textViewRepoDescription);
        String reposJSON = getIntent().getStringExtra("repo");
        repo = new Gson().fromJson(reposJSON, Repos.class);

        issueAdapter = new IssueAdapter();
        issueRecyclerView = findViewById(R.id.issueRecyclerView);
        issueRecyclerView.setLayoutManager(new LinearLayoutManager(RepoDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        issueRecyclerView.setAdapter(issueAdapter);
        reposPresenter = new ReposPresenter(RepoDetailActivity.this);
        reposPresenter.getIssues(repo.getOwner().getName(),repo.getName());

        Picasso.get().load(repo.getOwner().getAvatarUrl()).into(imageView);
        textViewRepoName.setText(repo.getName());
        textViewRepoDescription.setText(repo.getDescription());


    }

    @Override
    public void showReposOnUI(ReposPayload reposPayload) {

    }

    @Override
    public void showUserOnUI(User user) {

    }

    @Override
    public void showErrorOnUI(Throwable t) {

    }

    @Override
    public void showErrorOnUI(int resId) {

    }

    @Override
    public void showIssueOnUI(IssuePayload issuePayload) {
        issueAdapter.updateList(issuePayload);
    }

    @Override
    public Activity getActivityContext() {
        return null;
    }
}