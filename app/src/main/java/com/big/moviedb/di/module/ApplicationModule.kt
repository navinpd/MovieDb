package com.big.moviedb.di.module

import android.content.Context
import android.content.SharedPreferences
import com.big.moviedb.BuildConfig
import com.big.moviedb.MyApplication
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.Networking
import com.big.moviedb.di.ApplicationContext
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApplication) {


    @Provides
    @ApplicationContext
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()


    @Provides
    @Singleton
    fun provideGlide() : RequestManager = Glide.with(application)

    @Provides
    @Singleton
    fun provideNetworking(): NetworkService {
        return Networking.createNetworking(
            BuildConfig.API_Key,
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferences =
        application.getSharedPreferences("Local-Shared-Pref", Context.MODE_PRIVATE)

}
