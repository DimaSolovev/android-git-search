package com.dima.githubsearch.presenter;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dima.githubsearch.R;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.api.ApiService;
import com.dima.githubsearch.models.Issue;
import com.dima.githubsearch.models.Repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiService apiService = ApiFactory.getInstance().getApiService();
    private int page = 1;
    private final MutableLiveData<Boolean> shouldClosePrBar = new MutableLiveData();
    private final MutableLiveData<List<Repo>> repos = new MutableLiveData();
    private final MutableLiveData<List<Issue>> issues = new MutableLiveData<>();
    private final List<Repo> repoList = new ArrayList<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getShouldClosePrBar() {
        return shouldClosePrBar;
    }

    public MutableLiveData<List<Repo>> getRepos() {
        return repos;
    }

    public MutableLiveData<List<Issue>> getIssues() {
        return issues;
    }

    public void clearRepoPayload() {
        page = 1;
        repoList.clear();
        repos.postValue(new ArrayList<>());
    }

    public void searchRepos(String q) {
        Disposable disposable = apiService
                .searchRepos(q, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.postValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.postValue(true))
                .doOnError(throwable -> Toast.makeText(
                        getApplication(),
                        R.string.error_repo_limit,
                        Toast.LENGTH_LONG).show())
                .subscribe(
                        reposPayload -> {
                            page++;
                            repoList.addAll(reposPayload.getItems());
                            repos.setValue(repoList);
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void getIssues(String userName, String repoName) {
        Disposable disposable = apiService
                .getIssues(userName, repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.setValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.setValue(true))
                .doOnError(throwable -> Toast.makeText(
                        getApplication(),
                        R.string.error_repo_limit,
                        Toast.LENGTH_LONG).show())
                .subscribe(
                        issues::setValue
                );
        compositeDisposable.add(disposable);
    }

    public void loadDefaultRepos() {

        Disposable disposable = apiService
                .getRepos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> shouldClosePrBar.postValue(false))
                .doAfterTerminate(() -> shouldClosePrBar.postValue(true))
                .doOnError(throwable -> Toast.makeText(
                        getApplication(),
                        R.string.error_default_repo,
                        Toast.LENGTH_LONG).show())
                .subscribe(
                        repos::setValue
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}