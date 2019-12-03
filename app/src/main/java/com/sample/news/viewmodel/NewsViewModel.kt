package com.sample.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.news.interactor.NewsInteractor
import com.sample.news.model.NewsData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class NewsViewModel @Inject constructor(val newsInteractor: NewsInteractor) : ViewModel() {

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val showLoadingProgressLiveData = MutableLiveData<Boolean>()
    val loadingProgressDialogObservable: LiveData<Boolean>
        get() = showLoadingProgressLiveData

    private val errorProgressLiveData = MutableLiveData<String>()
    val errorObservable: LiveData<String>
        get() = errorProgressLiveData

    private val newsListLiveData = MutableLiveData<NewsData>()
    val newsListObservable: LiveData<NewsData>
        get() = newsListLiveData

    fun fetchNewsList() {
        showLoadingProgressLiveData.postValue(true)
        val disposable = newsInteractor.fetchNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showLoadingProgressLiveData.postValue(false)
                newsListLiveData.postValue(it)
            },
                {
                    it
                    showLoadingProgressLiveData.postValue(false)
                    errorProgressLiveData.postValue(null)
                })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        if (compositeDisposable != null) {
            compositeDisposable.clear()
        }
    }
}
