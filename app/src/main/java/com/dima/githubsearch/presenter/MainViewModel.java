package com.dima.githubsearch.presenter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dima.githubsearch.R;
import com.dima.githubsearch.api.ApiFactory;
import com.dima.githubsearch.api.ApiService;
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
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
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

    public void clearRepoPayload() {
        page = 1;
        repoList.clear();
        repos.postValue(new ArrayList<>());
    }

    public void searchRepos(String q) {
        Boolean loading = isLoading.getValue();
        if (loading != null && loading) {
            return;
        }
        Disposable disposable = apiService
                .searchRepos(q, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(scheduler -> isLoading.postValue(true))
                .doAfterTerminate(() -> isLoading.postValue(false))
                .subscribe(
                        reposPayload -> {
                            Log.d("MainViewModel", String.valueOf(page));
                            page++;
                            repoList.addAll(reposPayload.getItems());
                            repos.setValue(repoList);
                        }, throwable -> {
                            if (throwable.getMessage().contains("HTTP 403")) {
                                Toast.makeText(
                                        getApplication(),
                                        R.string.error_repo_limit,
                                        Toast.LENGTH_SHORT).show();
                            } else if (throwable.getMessage().contains("HTTP 422")) {
                                Toast.makeText(
                                        getApplication(),
                                        R.string.error_results_limit,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(
                                        getApplication(),
                                        throwable.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}