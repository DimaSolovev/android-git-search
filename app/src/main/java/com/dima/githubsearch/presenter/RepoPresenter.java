package com.dima.githubsearch.presenter;

import android.util.Log;

import com.dima.githubsearch.R;
import com.dima.githubsearch.activity.IActivity;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.RepoPayload;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RepoPresenter {

    private final ApiFactory apiFactory;
    private final IActivity mIActivity;
    private final CompositeDisposable compositeDisposable;
    private final RepoPayload repoPayload = new RepoPayload();
    private int page = 1;

    public RepoPresenter(IActivity iActivity) {
        mIActivity = iActivity;
        compositeDisposable = new CompositeDisposable();
        apiFactory = ApiFactory.getInstance();
    }

    public void clearRepoPayload() {
        page = 1;
        repoPayload.clearItems();
    }

    public void searchRepos(String q) {
        Disposable disposable = apiFactory
                .getApiService()
                .searchRepos(q, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reposPayload -> {
                            page++;
                            repoPayload.addItems(reposPayload.getItems());
                            mIActivity.showReposOnUI(repoPayload);
                            mIActivity.hideProgressBar();
                        },
                        throwable -> mIActivity.showErrorOnUI(R.string.error_rate_limit)
                );
        compositeDisposable.add(disposable);
    }

    public void getIssues(String userName, String repoName) {
        Disposable disposable = apiFactory
                .getApiService()
                .getIssues(userName, repoName)
                .subscribeOn(Schedulers.io())
                .map(issues -> {
                    IssuePayload issuePayload = new IssuePayload();
                    issuePayload.setItems(issues);
                    return issuePayload;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mIActivity::showIssueOnUI,
                        throwable -> mIActivity.showErrorOnUI(R.string.error_rate_limit)
                );
        compositeDisposable.add(disposable);
    }

    public void loadDefaultRepos() {

        Disposable disposable = apiFactory
                .getApiService()
                .getRepos()
                .subscribeOn(Schedulers.io())
                .map(repos -> {
                    RepoPayload reposPayload = new RepoPayload();
                    reposPayload.setItems(repos);
                    return reposPayload;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repoPayload -> {
                            mIActivity.hideProgressBar();
                            mIActivity.showReposOnUI(repoPayload);
                        },
                        throwable -> {
                            mIActivity.hideProgressBar();
                            mIActivity.showErrorOnUI(R.string.error_default_repo);
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void onStop() {
        compositeDisposable.dispose();
    }
}