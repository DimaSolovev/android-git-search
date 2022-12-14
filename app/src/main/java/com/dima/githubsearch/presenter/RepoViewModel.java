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

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RepoViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiService apiService = ApiFactory.getInstance().getApiService();
    private final MutableLiveData<List<Issue>> issues = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<List<Issue>> getIssues() {
        return issues;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public RepoViewModel(@NonNull Application application) {
        super(application);
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
                        , throwable -> {
                            if (throwable.getMessage().contains("HTTP 403")) {
                                Toast.makeText(
                                        getApplication(),
                                        R.string.error_issue_limit,
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
