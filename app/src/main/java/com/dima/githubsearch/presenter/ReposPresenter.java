package com.dima.githubsearch.presenter;

import com.dima.githubsearch.activity.IActivity;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.entity.IssuePayload;
import com.dima.githubsearch.entity.ReposPayload;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ReposPresenter {

    private ApiFactory apiFactory;
    private IActivity mIActivity;
    private CompositeDisposable compositeDisposable;

    public ReposPresenter(IActivity iActivity) {
        mIActivity = iActivity;
        compositeDisposable = new CompositeDisposable();
        apiFactory = apiFactory.getInstance();
    }

    public void searchRepos(String q) {
        Disposable disposable = apiFactory
                .getApiService()
                .searchRepos(q)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reposPayload -> mIActivity.showReposOnUI(reposPayload),
                        throwable -> mIActivity.showErrorOnUI(throwable.getCause())

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
                        issuePayloads -> mIActivity.showIssueOnUI(issuePayloads),
                        throwable -> mIActivity.showErrorOnUI(throwable.getCause())
                );
        compositeDisposable.add(disposable);
    }

    public void onStop() {
        compositeDisposable.dispose();
    }
}