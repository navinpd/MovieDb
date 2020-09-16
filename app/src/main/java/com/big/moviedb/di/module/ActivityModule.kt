package com.big.moviedb.di.module

import android.content.Context
import com.big.moviedb.di.ActivityContext
import com.big.moviedb.ui.MainActivity
import dagger.Module
import dagger.Provides

@Module
public class ActivityModule(private val mainActivity: MainActivity) {


    @Provides
    @ActivityContext
    fun provideContext(): Context = mainActivity


}