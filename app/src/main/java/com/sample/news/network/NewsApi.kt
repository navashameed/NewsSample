package com.sample.news.network

import com.sample.news.model.NewsData
import io.reactivex.Single
import retrofit2.http.GET

interface NewsApi {

    @GET("s/2iodh4vg0eortkl/facts.js")
    fun fetchNews(): Single<NewsData?>

}
