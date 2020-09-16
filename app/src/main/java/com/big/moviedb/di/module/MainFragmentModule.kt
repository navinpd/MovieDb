package com.big.moviedb.di.module

import android.content.Context
import com.big.moviedb.di.ActivityContext
import com.big.moviedb.ui.main.MainFragment
import dagger.Module
import dagger.Provides

@Module
class MainFragmentModule(private val fragment: MainFragment) {

    @ActivityContext
    @Provides
    fun provideContext(): Context = fragment.context!!
}
