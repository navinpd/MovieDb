package com.big.moviedb.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.big.moviedb.BuildConfig
import com.big.moviedb.data.Repository
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject


class HomeViewModel @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        private val repository: Repository) {

    companion object {
        const val MOVIE_KEY = "Movie_Names"
        const val TAG: String = "HomeViewModel"
    }

    var getSearchResults = repository.mutableLiveData


    fun getSearchResult(query: String, pageNumber: Int) {
        repository.getServerResponse(query, pageNumber)

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