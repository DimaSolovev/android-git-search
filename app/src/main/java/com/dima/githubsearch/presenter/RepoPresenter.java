package com.dima.githubsearch.presenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dima.githubsearch.R;
import com.dima.githubsearch.activity.IActivity;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.models.Issue;
import com.dima.githubsearch.models.Repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RepoPresenter {

    private final ApiFactory apiFactory;
    private final IActivity mIActivity;
    private final CompositeDisposable compositeDisposable;
    private int page = 1;
    private MutableLiveData<Boolean> shouldClosePrBar = new MutableLiveData();
    private MutableLiveData<List<Repo>> repos = new MutableLiveData();
    private MutableLiveData<List<Issue>> issues = new MutableLiveData<>();

    public LiveData<Boolean> getShouldClosePrBar() {
        return shouldClosePrBar;
    }

    public MutableLiveData<List<Repo>> getRepos() {
        return repos;
    }

    public MutableLiveData<List<Issue>> getIssues() {
        return issues;
    }

    public RepoPresenter(IActivity iActivity) {
        mIActivity = iActivity;
        compositeDisposable = new CompositeDisposable();
        apiFactory = ApiFactory.getInstance();
    }

    public void clearRepoPayload() {
        page = 1;
        repos.postValue(new ArrayList<>());
    }

    public void searchRepos(String q) {
        Disposable disposable = apiFactory
                .getApiService()
                .searchRepos(q, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.postValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.postValue(true))
                .subscribe(
                        reposPayload -> {
                            page++;
                            repos.setValue(reposPayload.getItems());
                        },
                        throwable -> mIActivity.showErrorOnUI(R.string.error_repo_limit)
                );
        compositeDisposable.add(disposable);
    }

    public void getIssues(String userName, String repoName) {
        Disposable disposable = apiFactory
                .getApiService()
                .getIssues(userName, repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.setValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.setValue(true))
                .subscribe(
                        issuesFromNet -> {
                            issues.setValue(issuesFromNet);
                        },
                        throwable -> mIActivity.showErrorOnUI(R.string.error_issue_limit)
                );
        compositeDisposable.add(disposable);
    }

    public void loadDefaultRepos() {

        Disposable disposable = apiFactory
                .getApiService()
                .getRepos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.postValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.postValue(true))
                .subscribe(
                        repoList -> repos.setValue(repoList),
                        throwable -> mIActivity.showErrorOnUI(R.string.error_default_repo)
                );
        compositeDisposable.add(disposable);
    }

    public void onStop() {
        compositeDisposable.dispose();
    }
}