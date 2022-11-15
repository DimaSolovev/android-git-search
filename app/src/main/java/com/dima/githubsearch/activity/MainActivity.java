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
import com.dima.githubsearch.presenter.MainViewModel;
import com.dima.githubsearch.utils.Utils;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MainViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private RepoAdapter repoAdapter;
    private int charSequenceLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setSupportActionBar(toolbar);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeViewModel();
        repoAdapter.setOnRepoClickListener(repo -> {
            Intent intent = RepoDetailActivity.newIntent(MainActivity.this, repo);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search repo...");
        registerToSearchViewEvents(searchView);
        return true;
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        repoAdapter = new RepoAdapter();
        recyclerView.setAdapter(repoAdapter);
        compositeDisposable = new CompositeDisposable();
    }

    public void registerToSearchViewEvents(SearchView searchView) {

        Disposable disposable = RxSearchView
                .queryTextChanges(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(chars -> chars.toString().trim())
                .filter(text -> {
                    if (text.isEmpty()) {
                        viewModel.clearRepoPayload();
                        return false;
                    }
                    return true;
                })
                .distinctUntilChanged()
                .subscribe(text -> {
                    if (text.length() != charSequenceLength) {
                        viewModel.clearRepoPayload();
                        charSequenceLength = text.length();
                    }
                    viewModel.searchRepos(text);
                    repoAdapter.setOnReachEndListener(() -> viewModel.searchRepos(text)
                    );
                }, throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
        compositeDisposable.add(disposable);
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this,
                isLoading -> Utils.shouldClosePrBar(isLoading, progressBar));
        viewModel.getRepos().observe(this, repoList -> repoAdapter.setRepos(repoList));
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}