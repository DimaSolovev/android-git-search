package com.dima.githubsearch.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.IssueAdapter;
import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.Repo;
import com.dima.githubsearch.models.RepoPayload;
import com.dima.githubsearch.presenter.RepoPresenter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class RepoDetailActivity extends AppCompatActivity implements IActivity {

    private IssueAdapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewRepoName = findViewById(R.id.textViewRepoName);
        TextView textViewRepoDescription = findViewById(R.id.textViewRepoDescription);
        String reposJSON = getIntent().getStringExtra("repo");
        Repo repo = new Gson().fromJson(reposJSON, Repo.class);

        Picasso.get().load(repo.getOwner().getAvatarUrl()).into(imageView);
        textViewRepoName.setText(repo.getName());
        textViewRepoDescription.setText(repo.getDescription());

        issueAdapter = new IssueAdapter();
        RecyclerView issueRecyclerView = findViewById(R.id.issueRecyclerView);
        issueRecyclerView.setLayoutManager(new LinearLayoutManager(RepoDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        issueRecyclerView.setAdapter(issueAdapter);
        RepoPresenter repoPresenter = new RepoPresenter(RepoDetailActivity.this);
        repoPresenter.getIssues(repo.getOwner().getLogin(), repo.getName());
    }

    @Override
    public void showReposOnUI(RepoPayload repoPayload) {

    }

    @Override
    public void showErrorOnUI(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showIssueOnUI(IssuePayload issuePayload) {
        issueAdapter.updateList(issuePayload);
    }

}