package com.dima.githubsearch.presenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dima.githubsearch.R;
import com.dima.githubsearch.activity.IActivity;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.models.IssuePayload;
import com.dima.githubsearch.models.RepoPayload;

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
    private MutableLiveData<Boolean> shouldClosePrBar = new MutableLiveData();

    public LiveData<Boolean> getShouldClosePrBar() {
        return shouldClosePrBar;
    }

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
                .doOnSubscribe(disposable1 -> shouldClosePrBar.postValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.postValue(true))
                .subscribe(
                        reposPayload -> {
                            page++;
                            repoPayload.addItems(reposPayload.getItems());
                            mIActivity.showReposOnUI(repoPayload);
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
                .map(issues -> {
                    IssuePayload issuePayload = new IssuePayload();
                    issuePayload.setItems(issues);
                    return issuePayload;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.setValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.setValue(true))
                .subscribe(
                        mIActivity::showIssueOnUI,
                        throwable -> mIActivity.showErrorOnUI(R.string.error_issue_limit)
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
                .doOnSubscribe(disposable1 -> shouldClosePrBar.postValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.postValue(true))
                .subscribe(
                        mIActivity::showReposOnUI,
                        throwable -> mIActivity.showErrorOnUI(R.string.error_default_repo)
                );
        compositeDisposable.add(disposable);
    }

    public void onStop() {
        compositeDisposable.dispose();
    }
}