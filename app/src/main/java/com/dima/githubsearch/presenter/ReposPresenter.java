package com.dima.githubsearch.presenter;

import com.dima.githubsearch.activity.IActivity;
import com.dima.githubsearch.api.ApiFactory;
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
                .subscribe(new Consumer<ReposPayload>() {
                               @Override
                               public void accept(ReposPayload reposPayload) throws Exception {
                                   mIActivity.showReposOnUI(reposPayload);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   mIActivity.showErrorOnUI(throwable.getCause());
                               }
                           }

                );
        compositeDisposable.add(disposable);
    }

}
