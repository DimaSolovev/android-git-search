package com.dima.githubsearch.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.RepoAdapter;
import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.RepoPayload;
import com.dima.githubsearch.presenter.RepoPresenter;
import com.google.gson.Gson;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements IActivity {

    private RepoPresenter repoPresenter;
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
        repoAdapter = new RepoAdapter(MainActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(repoAdapter);
        repoPresenter = new RepoPresenter(MainActivity.this);
        repoAdapter.setOnClickListener(id -> {
            Intent intent = RepoDetailActivity.newIntent(MainActivity.this);
            intent.putExtra("repo", new Gson().toJson(repoAdapter.getReposPayload().getItems().get(id)));
            startActivity(intent);
        });
        repoPresenter.getShouldClosePrBar().observe(this, this::shouldClosePrBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            registerToSearchViewEvents(searchView);
        }
        return true;
    }

    public void registerToSearchViewEvents(SearchView searchView) {

        Disposable disposable = RxSearchView
                .queryTextChanges(searchView)
                .doOnSubscribe(disposable1 -> shouldClosePrBar(false))
                .debounce(400, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map(chars -> chars.toString().trim())
                .filter(text -> {
                    if (text.isEmpty()) {
                        charSequenceLength = 0;
                        repoPresenter.loadDefaultRepos();
                    }
                    return !text.isEmpty();
                })
                .subscribe(text -> {
                    if (text.length() != charSequenceLength) {
                        repoPresenter.clearRepoPayload();
                        charSequenceLength = text.length();
                    }
                    repoPresenter.searchRepos(text);
                    repoAdapter.setOnReachEndListener(() -> {
                                repoPresenter.searchRepos(text);
                            }
                    );
                }, throwable -> {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void showReposOnUI(RepoPayload repoPayload) {
        repoAdapter.updateList(repoPayload);
    }

    @Override
    public void showErrorOnUI(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showIssueOnUI(IssuePayload issuePayload) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        repoPresenter.onStop();
    }

    private void shouldClosePrBar(Boolean shouldClose){
        if(shouldClose){
            progressBar.setVisibility(View.INVISIBLE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}