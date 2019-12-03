package com.sample.news.interactor

import com.sample.news.model.NewsData
import com.sample.news.network.NewsApi
import io.reactivex.Single

class NewsInteractor(val newsApi: NewsApi) {

    fun fetchNews(): Single<NewsData?> {
        return newsApi.fetchNews()
    }

}