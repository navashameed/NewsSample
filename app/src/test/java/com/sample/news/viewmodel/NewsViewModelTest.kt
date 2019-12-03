package com.test.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.news.RxImmediateSchedulerRule
import com.sample.news.interactor.NewsInteractor
import com.sample.news.model.NewsData
import com.sample.news.viewmodel.NewsViewModel
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.*
import org.mockito.*


class NewsViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    lateinit var newsViewModel: NewsViewModel

    @Mock
    lateinit var newsInteractor: NewsInteractor

    @Captor
    lateinit var argCaptorQuery: ArgumentCaptor<String>

    var observableError = Single.error<NewsData>(Exception())

    @Mock
    lateinit var responseInstance: NewsData

    var testScheduler = TestScheduler()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        newsViewModel = NewsViewModel(newsInteractor)
    }

    @Test
    fun `WHEN ViewModel news list fetched THEN progress dialog is shown and interactor fetchNews is invoked`() {
        Mockito.`when`(newsInteractor.fetchNews())
            .thenReturn(
                Single.just(responseInstance).observeOn(TestScheduler()).subscribeOn(
                    TestScheduler()
                )
            )
        newsViewModel.fetchNewsList()
        Assert.assertEquals(newsViewModel.loadingProgressDialogObservable.value, true)
        Mockito.verify(newsInteractor).fetchNews()
    }

    //TODO Success and fialure scenarios

}
