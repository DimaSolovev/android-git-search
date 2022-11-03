package com.dima.githubsearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dima.githubsearch.R;
import com.dima.githubsearch.adapters.ReposSearchRecyclerViewAdapter;
import com.dima.githubsearch.entity.ReposPayload;
import com.dima.githubsearch.entity.User;
import com.dima.githubsearch.presenter.ReposPresenter;

import java.util.concurrent.TimeUnit;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements IActivity{

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private ReposPresenter mReposPresenter;
    private CompositeSubscription mCompositeSubscription;
    private ReposSearchRecyclerViewAdapter mReposSearchRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mReposSearchRecyclerViewAdapter = new ReposSearchRecyclerViewAdapter(MainActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(this.mReposSearchRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this, "Search is expanded", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(MainActivity.this, "Search is Collapse", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        menu.findItem(R.id.action_search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Enter repo name...");
        return true;
    }

    public void registerToSearchViewEvents(SearchView searchView) {
        this.mCompositeSubscription.add(
                RxSearchView
                        .queryTextChanges(searchView)
                        .throttleLast(100, TimeUnit.MILLISECONDS)
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .onBackpressureLatest()
                        .filter(charSequence -> {
                            if (TextUtils.isEmpty(charSequence)) {
                                this.mReposPresenter.loadDefaultRepos();
                            }
                            return !TextUtils.isEmpty(charSequence);
                        })
                        .subscribe(charSequence -> mReposPresenter.searchRepos(charSequence.toString()))
        );
    }

    @Override
    public void showReposOnUI(ReposPayload reposPayload) {
        mReposSearchRecyclerViewAdapter.updateList(reposPayload);
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
}