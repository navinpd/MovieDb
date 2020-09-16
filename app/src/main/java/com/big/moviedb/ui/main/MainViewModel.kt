package com.big.moviedb.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.big.moviedb.BuildConfig
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayDeque
import javax.inject.Inject


class HomeViewModel @Inject constructor(
        private val compositeDisposable: CompositeDisposable,
        private val networkService: NetworkService,
        private val sharedPreferences: SharedPreferences) {

    companion object {
        val TAG: String = "HomeViewModel"
    }

    val getSearchResults = MutableLiveData<MovieResults>()
    private val queue = ArrayDeque<String>()
    private val pageNumber = 1;

    fun getSearchResult(query: String) {

        compositeDisposable.add(
                networkService.searchImages(
                        apiKey = BuildConfig.API_Key,
                        querySearch = query,
                        pageNumber = pageNumber
                )
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    getSearchResults.postValue(it)
                                    //TODO: Save this data in caching
                                    Log.d(TAG, it.toString())
                                },
                                {
                                    queue.poll()
                                    getSearchResults.postValue(null)
                                    Log.d(TAG, it.toString())
                                }
                        )
        )
    }


    fun checkIfDataIsInLocal(query: String): String? =
            if (sharedPreferences.contains(query)) sharedPreferences.getString(query, "") else ""

    fun saveDataInLocal(key: String, data: String) =
            sharedPreferences.edit().putString(key, data).apply()

}