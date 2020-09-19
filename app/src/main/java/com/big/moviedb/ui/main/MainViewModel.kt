package com.big.moviedb.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.big.moviedb.BuildConfig
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import com.google.gson.GsonBuilder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class HomeViewModel @Inject constructor(
        private val compositeDisposable: CompositeDisposable,
        private val networkService: NetworkService,
        private val sharedPreferences: SharedPreferences) {
    private val gson = GsonBuilder().create()

    companion object {
        const val MOVIE_KEY = "Movie_Names"
        const val TAG: String = "HomeViewModel"
    }

    val getSearchResults = MutableLiveData<MovieResults>()
    private val queue = ArrayDeque<String>()
    fun getSearchResult(query: String, pageNumber: Int) {

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
                                    saveDataInLocal(query)
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


    fun getListFromLocal(): MutableSet<String>? = sharedPreferences.getStringSet(MOVIE_KEY, null)


    private fun saveDataInLocal(data: String): Boolean {

        var set = getListFromLocal()
        if (set == null) {
            set = mutableSetOf(data)
        } else {
            set.add(data)
        }

        return sharedPreferences.edit().putStringSet(MOVIE_KEY, set).commit()
    }

}