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
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData(false);
    private final MutableLiveData<List<Repo>> repos = new MutableLiveData();
    private final MutableLiveData<List<Issue>> issues = new MutableLiveData<>();
    private final List<Repo> repoList = new ArrayList<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Repo>> getRepos() {
        return repos;
    }

    public LiveData<List<Issue>> getIssues() {
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
                .doOnSubscribe(scheduler -> isLoading.postValue(true))
                .doAfterTerminate(() -> isLoading.postValue(false))
                .subscribe(
                        reposPayload -> {
                            page++;
                            repoList.addAll(reposPayload.getItems());
                            repos.setValue(repoList);
                        }, throwable -> Toast.makeText(
                                getApplication(),
                                R.string.error_repo_limit,
                                Toast.LENGTH_LONG).show());
        compositeDisposable.add(disposable);
    }

    public void getIssues(String userName, String repoName) {
        Disposable disposable = apiService
                .getIssues(userName, repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(scheduler -> isLoading.setValue(true))
                .doAfterTerminate(() -> isLoading.setValue(false))
                .subscribe(
                        issues::setValue
                        , throwable -> Toast.makeText(
                                getApplication(),
                                R.string.error_issue_limit,
                                Toast.LENGTH_LONG).show());
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}