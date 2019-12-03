package com.sample.nytimesapp.interactor


import com.sample.news.interactor.NewsInteractor
import com.sample.news.model.NewsData
import com.sample.news.network.NewsApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class NewsInteractorTest {

    lateinit var newsApi: NewsApi

    lateinit var newsInteractor: NewsInteractor

    @Mock
    lateinit var newsData: Single<NewsData?>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        newsApi = Mockito.mock(NewsApi::class.java)
        newsInteractor =
            NewsInteractor(newsApi)
    }

    @Test
    fun `WHEN interactor fetch news called THEN should trigger api call for fetch news`() {
        Mockito.`when`(newsApi.fetchNews())
            .thenReturn(newsData)

        newsInteractor.fetchNews()
        Mockito.verify(newsApi).fetchNews()
    }
}
