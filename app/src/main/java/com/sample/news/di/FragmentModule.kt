package com.sample.news.di

import com.sample.news.di.annotation.PerFragment
import com.sample.news.view.fragment.NewsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @PerFragment
    @ContributesAndroidInjector(modules = [NewsModule::class])
    abstract fun bindListFragment(): NewsListFragment

//    @PerFragment
//    @ContributesAndroidInjector(modules = [NewsModule::class])
//    abstract fun bindDetailFragment(): NewsDetailFragment
}
