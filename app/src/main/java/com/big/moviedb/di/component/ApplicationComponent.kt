package com.big.moviedb.di.component

import android.content.Context
import android.content.SharedPreferences
import com.big.moviedb.MyApplication
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.di.ApplicationContext
import com.big.moviedb.di.module.ApplicationModule
import com.bumptech.glide.RequestManager
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: MyApplication)

    @ApplicationContext
    fun getContext(): Context

    fun getCompositeDisposable(): CompositeDisposable

    fun getNetworkService(): NetworkService

    fun getPrefStore(): SharedPreferences

    fun getGlide(): RequestManager

}