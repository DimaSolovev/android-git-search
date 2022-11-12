package com.dima.githubsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.IssueAdapter;
import com.dima.githubsearch.models.Issue;
import com.dima.githubsearch.models.Repo;
import com.dima.githubsearch.presenter.MainViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepoDetailActivity extends AppCompatActivity {

    private IssueAdapter issueAdapter;
    private ProgressBar progressBarIssue;
    private static final String REPO = "repo";
    private TextView textViewIssueNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        progressBarIssue = findViewById(R.id.progressBarIssue);
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewRepoName = findViewById(R.id.textViewRepoName);
        TextView textViewRepoDescription = findViewById(R.id.textViewRepoDescription);
        textViewIssueNotFound = findViewById(R.id.textViewIssueNotFound);

        Intent intent = getIntent();
        String reposJSON = null;
        if (intent != null && intent.hasExtra(REPO)) {
            reposJSON = intent.getStringExtra(REPO);
        } else {
            finish();
        }
        Repo repo = new Gson().fromJson(reposJSON, Repo.class);

        Picasso.get().load(repo.getOwner().getAvatarUrl()).placeholder(R.drawable.def).into(imageView);
        textViewRepoName.setText(repo.getFullName());
        textViewRepoDescription.setText(repo.getDescription());

        issueAdapter = new IssueAdapter();
        RecyclerView issueRecyclerView = findViewById(R.id.issueRecyclerView);
        issueRecyclerView.setAdapter(issueAdapter);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getShouldClosePrBar().observe(this, this::shouldClosePrBar);
        viewModel.getIssues(repo.getOwner().getLogin(), repo.getName());
        viewModel.getIssues().observe(this, issues -> {
            if (issues.isEmpty()) {
                textViewIssueNotFound.setVisibility(View.VISIBLE);
            }
            issueAdapter.updateList(issues);
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RepoDetailActivity.class);
    }

    private void shouldClosePrBar(Boolean shouldClose) {
        if (shouldClose) {
            progressBarIssue.setVisibility(View.INVISIBLE);
        } else {
            progressBarIssue.setVisibility(View.VISIBLE);
        }
    }
}