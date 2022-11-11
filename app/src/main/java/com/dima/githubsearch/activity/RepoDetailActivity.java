package com.dima.githubsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.IssueAdapter;
import com.dima.githubsearch.models.Issue;
import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.Repo;
import com.dima.githubsearch.models.RepoPayload;
import com.dima.githubsearch.presenter.RepoPresenter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepoDetailActivity extends AppCompatActivity implements IActivity {

    private IssueAdapter issueAdapter;
    private ProgressBar progressBarIssue;
    private RepoPresenter repoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        progressBarIssue = findViewById(R.id.progressBarIssue);
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewRepoName = findViewById(R.id.textViewRepoName);
        TextView textViewRepoDescription = findViewById(R.id.textViewRepoDescription);

        Intent intent = getIntent();
        String reposJSON = null;
        if (intent != null && intent.hasExtra("repo")) {
            reposJSON = intent.getStringExtra("repo");
        } else {
            finish();
        }
        Repo repo = new Gson().fromJson(reposJSON, Repo.class);

        Picasso.get().load(repo.getOwner().getAvatarUrl()).placeholder(R.drawable.def).into(imageView);
        textViewRepoName.setText(repo.getName());
        textViewRepoDescription.setText(repo.getDescription());

        issueAdapter = new IssueAdapter();
        RecyclerView issueRecyclerView = findViewById(R.id.issueRecyclerView);
        issueRecyclerView.setAdapter(issueAdapter);
        repoPresenter = new RepoPresenter(RepoDetailActivity.this);
        repoPresenter.getShouldClosePrBar().observe(this, this::shouldClosePrBar);
        repoPresenter.getIssues(repo.getOwner().getLogin(), repo.getName());
        repoPresenter.getIssues().observe(this, new Observer<List<Issue>>() {
            @Override
            public void onChanged(List<Issue> issues) {
                issueAdapter.updateList(issues);
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RepoDetailActivity.class);
    }

    @Override
    public void showErrorOnUI(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repoPresenter.onStop();
    }

    private void shouldClosePrBar(Boolean shouldClose) {
        if (shouldClose) {
            progressBarIssue.setVisibility(View.INVISIBLE);
        } else {
            progressBarIssue.setVisibility(View.VISIBLE);
        }
    }
}