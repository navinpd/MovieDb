package com.big.moviedb.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.big.moviedb.BuildConfig
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class HomeViewModel @Inject constructor(
        private val compositeDisposable: CompositeDisposable,
        private val networkService: NetworkService,
        private val sharedPreferences: SharedPreferences) {

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
                ).subscribeOn(Schedulers.io())
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

    fun getListFromLocal(): ArrayList<String>? {

        val data = sharedPreferences.getString(MOVIE_KEY, "")
        return ArrayList<String>(data!!.split(","))
    }

    fun saveDataInLocal(data: String) {
        if (data.isEmpty()) {
            return
        }
        val localMovieData = getListFromLocal()

        if (localMovieData!!.contains(data))
            return

        localMovieData!!.add(0, data)
        val regex = ","
        val joined: String = join(regex, localMovieData)

        return sharedPreferences.edit().putString(MOVIE_KEY, joined).apply()
    }

    private fun join(delimiter: CharSequence, tokens: Iterable<*>): String {
        val it = tokens.iterator()
        if (!it.hasNext()) {
            return ""
        }
        val sb = StringBuilder()
        sb.append(it.next())
        while (it.hasNext()) {
            sb.append(delimiter)
            sb.append(it.next())
        }
        return sb.toString()
    }

}