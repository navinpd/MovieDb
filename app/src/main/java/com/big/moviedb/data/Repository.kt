package com.big.moviedb.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.big.moviedb.BuildConfig
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import retrofit2.Callback
import javax.inject.Inject


class Repository @Inject constructor(private val networkService: NetworkService) {
    private val TAG = "Repository"
    val mutableLiveData: MutableLiveData<MovieResults> = MutableLiveData()

    public fun getServerResponse(query: String, pageNumber: Int) {
        val data = networkService.searchImages(
                apiKey = BuildConfig.API_Key,
                querySearch = query,
                pageNumber = pageNumber)

        data.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: retrofit2.Call<MovieResults>,
                                    response: retrofit2.Response<MovieResults>) {

                if (!response.isSuccessful || response.code() != 200) {
                    mutableLiveData.setValue(null)
                } else {
                    mutableLiveData.setValue(response.body())
                }
            }

            override fun onFailure(call: retrofit2.Call<MovieResults>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
                mutableLiveData.setValue(null)
            }
        });
    }
}