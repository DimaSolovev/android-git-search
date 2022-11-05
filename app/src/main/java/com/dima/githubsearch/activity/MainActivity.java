package com.dima.githubsearch.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.ReposSearchAdapter;
import com.dima.githubsearch.entity.IssuePayload;
import com.dima.githubsearch.entity.ReposPayload;
import com.dima.githubsearch.entity.User;
import com.dima.githubsearch.presenter.ReposPresenter;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements IActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ReposPresenter reposPresenter;
    private CompositeDisposable compositeDisposable;
    private ReposSearchAdapter reposSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reposSearchAdapter = new ReposSearchAdapter(MainActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(this.reposSearchAdapter);
        reposPresenter = new ReposPresenter(MainActivity.this);
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
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> reposPresenter.searchRepos(charSequence.toString()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void showReposOnUI(ReposPayload reposPayload) {
        reposSearchAdapter.updateList(reposPayload);
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
    public Activity getActivityContext() {
        return null;
    }

    @Override
    public void showIssueOnUI(IssuePayload issuePayload) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        reposPresenter.onStop();
    }
}