package com.dima.githubsearch.presenter;

import com.dima.githubsearch.activity.IActivity;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.ReposPayload;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReposPresenter {

    private ApiFactory apiFactory;
    private IActivity mIActivity;
    private CompositeDisposable compositeDisposable;
    private ReposPayload mReposPayload = new ReposPayload();
    private int page = 1;

    public ReposPresenter(IActivity iActivity) {
        mIActivity = iActivity;
        compositeDisposable = new CompositeDisposable();
        apiFactory = apiFactory.getInstance();
    }

    public void searchRepos(String q) {
        Disposable disposable = apiFactory
                .getApiService()
                .searchRepos(q, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reposPayload ->{
                            page++;
                            mReposPayload.addItems(reposPayload.getItems());
                            mIActivity.showReposOnUI(mReposPayload);
                        },
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