package com.sample.news.di

import androidx.lifecycle.ViewModel
import com.sample.news.di.annotation.PerFragment
import com.sample.news.interactor.NewsInteractor
import com.sample.news.network.NewsApi
import com.sample.news.util.ViewModelKey
import com.sample.news.viewmodel.NewsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(includes = [NewsModule.BindsModule::class])
class NewsModule {

    @Provides
    @PerFragment
    fun providesNewsInteractor(newsApi: NewsApi): NewsInteractor {
        return NewsInteractor(newsApi)
    }

    @Provides
    @PerFragment
    fun providesNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Module
    abstract class BindsModule {
        @Binds
        @IntoMap
        @ViewModelKey(NewsViewModel::class)
        abstract fun bindNewsViewModel(viewViewModel: NewsViewModel): ViewModel
    }

}
