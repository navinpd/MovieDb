package com.big.moviedb

import android.app.Application
import android.content.SharedPreferences
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.di.component.ApplicationComponent
import com.big.moviedb.di.component.DaggerApplicationComponent
import com.big.moviedb.di.module.ApplicationModule
import javax.inject.Inject

class MyApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        getDependencies()
    }


    private fun getDependencies() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
        applicationComponent.inject(this)
    }
}