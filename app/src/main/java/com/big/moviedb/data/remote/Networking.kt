package com.big.moviedb.data.remote


import com.big.moviedb.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object Networking {

    lateinit var API_VAL: String
    lateinit var API_HOST_VAL: String


    fun createNetworking(apiVal: String, apiHost: String, baseUrl: String, cascheDir: File, cascheSize: Long): NetworkService {
        API_VAL = apiVal
        API_HOST_VAL = apiHost
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(
                        OkHttpClient.Builder()
                                .cache(Cache(cascheDir, cascheSize))
                                .addInterceptor(HttpLoggingInterceptor()
                                        .apply {
                                            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                                            else HttpLoggingInterceptor.Level.NONE
                                        })
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(
                        NetworkService::class.java
                )
    }

}