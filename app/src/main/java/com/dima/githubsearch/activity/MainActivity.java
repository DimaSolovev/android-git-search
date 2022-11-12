package com.dima.githubsearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.RepoAdapter;
import com.dima.githubsearch.models.Repo;
import com.dima.githubsearch.presenter.MainViewModel;
import com.google.gson.Gson;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private RepoAdapter repoAdapter;
    public ProgressBar progressBar;
    private int charSequenceLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        repoAdapter = new RepoAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(repoAdapter);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        repoAdapter.setOnClickListener(id -> {
            Intent intent = RepoDetailActivity.newIntent(MainActivity.this);
            intent.putExtra("repo", new Gson().toJson(repoAdapter.getRepoList().get(id)));
            startActivity(intent);
        });
        viewModel.getShouldClosePrBar().observe(this, this::shouldClosePrBar);
        viewModel.getRepos().observe(this, repoList -> repoAdapter.updateList(repoList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        registerToSearchViewEvents(searchView);
        return true;
    }

    public void registerToSearchViewEvents(SearchView searchView) {

        Disposable disposable = RxSearchView
                .queryTextChanges(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(chars -> chars.toString().trim())
                .distinctUntilChanged()
                .filter(text -> {
                    if (text.isEmpty()) {
                        viewModel.clearRepoPayload();
                    }
                    return !text.isEmpty();
                })
                .subscribe(text -> {
                    if (text.length() != charSequenceLength) {
                        viewModel.clearRepoPayload();
                        charSequenceLength = text.length();
                    }
                    viewModel.searchRepos(text);
                    repoAdapter.setOnReachEndListener(() -> viewModel.searchRepos(text)
                    );
                }, throwable -> {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void shouldClosePrBar(Boolean shouldClose) {
        if (shouldClose) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}