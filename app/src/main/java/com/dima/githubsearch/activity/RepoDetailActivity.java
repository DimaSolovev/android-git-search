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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.IssueAdapter;
import com.dima.githubsearch.models.Repo;
import com.dima.githubsearch.presenter.RepoViewModel;
import com.dima.githubsearch.utils.Utils;
import com.squareup.picasso.Picasso;

public class RepoDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewRepoName;
    private TextView textViewRepoDescription;
    private TextView textViewIssueNotFound;
    private ProgressBar progressBarIssue;
    private RecyclerView issueRecyclerView;
    private IssueAdapter issueAdapter;
    private static final String EXTRA_REPO = "repo";
    private RepoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        initViews();
        Intent intent = getIntent();
        Repo repo = null;
        if (intent != null && intent.hasExtra(EXTRA_REPO)) {
            repo = (Repo) getIntent().getSerializableExtra(EXTRA_REPO);
        } else {
            finish();
        }

        Picasso.get().load(repo.getOwner()
                .getAvatarUrl()).placeholder(R.drawable.def).into(imageView);
        textViewRepoName.setText(repo.getFullName());
        textViewRepoDescription.setText(repo.getDescription());

        viewModel = new ViewModelProvider(this).get(RepoViewModel.class);
        observeViewModel();
        viewModel.getIssues(repo.getOwner().getLogin(), repo.getName());
    }

    private void initViews() {
        imageView = findViewById(R.id.imageView);
        textViewRepoName = findViewById(R.id.textViewRepoName);
        textViewRepoDescription = findViewById(R.id.textViewRepoDescription);
        textViewIssueNotFound = findViewById(R.id.textViewIssueNotFound);
        progressBarIssue = findViewById(R.id.progressBarIssue);
        issueRecyclerView = findViewById(R.id.issueRecyclerView);
        issueAdapter = new IssueAdapter();
        issueRecyclerView.setAdapter(issueAdapter);
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this,
                isLoading -> Utils.shouldClosePrBar(isLoading, progressBarIssue));
        viewModel.getIssues().observe(this, issues -> {
            if (issues.isEmpty()) {
                textViewIssueNotFound.setVisibility(View.VISIBLE);
            }
            issueAdapter.updateList(issues);
        });
        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage.contains("HTTP 403")) {
                Toast.makeText(
                        RepoDetailActivity.this,
                        R.string.error_issue_limit,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        RepoDetailActivity.this,
                        errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent newIntent(Context context, Repo repo) {
        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtra(EXTRA_REPO, repo);
        return intent;
    }


}