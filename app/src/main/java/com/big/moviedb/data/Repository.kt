package com.big.moviedb.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.big.moviedb.BuildConfig
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import retrofit2.Callback
import javax.inject.Inject


class Repository @Inject constructor(private val networkService: NetworkService) {
    val TAG = "Repository"
    val mutableLiveData: MutableLiveData<MovieResults> = MutableLiveData()

    public fun getServerResponse(query: String, pageNumber: Int) {
        val data = networkService.searchImages(
                apiKey = BuildConfig.API_Key,
                querySearch = query,
                pageNumber = pageNumber)

        data.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: retrofit2.Call<MovieResults>,
                                    response: retrofit2.Response<MovieResults>) {
                Log.d(TAG, "ServerResponse is " + response.isSuccessful)
                Log.d(TAG, "ServerResponse successCode " + response.code())

                if (!response.isSuccessful || response.code() != 200) {
                    mutableLiveData.postValue(null)
                } else {
                    mutableLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: retrofit2.Call<MovieResults>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
                mutableLiveData.postValue(null)
            }
        });
    }
}