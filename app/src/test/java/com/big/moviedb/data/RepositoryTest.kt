package com.big.moviedb.data


import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.big.moviedb.BuildConfig.API_Key
import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(PowerMockRunner::class)
@PrepareForTest(Response::class, Call::class, NetworkService::class, Log::class)
internal class RepositoryTest {
    @Rule
    var instantRule = InstantTaskExecutorRule()


    private var repository: Repository? = null

    @Mock
    @JvmField
    val networkService: NetworkService? = null

    @Mock
    @JvmField
    var call: Call<MovieResults>? = null

    @Mock
    @JvmField
    var serverCall: Call<MovieResults>? = null

    @Mock
    @JvmField
    var response: Response<MovieResults>? = null

    @Test
    fun testServerResponse() {
        val observer: Observer<MovieResults> = mock(Observer::class.java) as Observer<MovieResults>

        // Mocking
        `when`(networkService!!.searchImages(apiKey = API_Key, querySearch = "Rocky", pageNumber = 1))
                .thenReturn(serverCall)

        `when`(serverCall!!.enqueue(any())).then {
            (it.arguments[0] as Callback<MovieResults>)
                    .onResponse(call, response)
        }

        // Mocking the server response
        `when`(response!!.code()).thenReturn(404)
        `when`(response!!.isSuccessful).thenReturn(true)

        // Actual Invocation
        repository = Repository(networkService!!)
        repository!!.mutableLiveData.observeForever(observer)
        repository!!.getServerResponse("Rocky", 1)

        // Verification
        verify(observer).onChanged(null)
        assertEquals(repository!!.mutableLiveData.value, null)
        assertEquals(response!!.code(), 404)
        assertEquals(response!!.isSuccessful, true)
    }


    @Before
    fun setUp() {
        PowerMockito.mockStatic(Response::class.java, Log::class.java)
        MockitoAnnotations.initMocks(this)
    }
}