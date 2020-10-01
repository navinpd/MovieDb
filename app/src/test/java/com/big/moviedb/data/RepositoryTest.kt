package com.big.moviedb.data


import com.big.moviedb.data.remote.NetworkService
import com.big.moviedb.data.remote.response.MovieResults
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(PowerMockRunner::class)
@PrepareForTest(Response::class, Call::class, NetworkService::class)
internal class RepositoryTest {

    @Mock
    @JvmField
    val networkService: NetworkService? = null
    var repository: Repository? = null

    @Mock
    @JvmField
    var serverCall: Call<MovieResults>? = null

    @Mock
    @JvmField
    var response: Response<MovieResults>? = null

    @Test
    fun testServerResponse() {

        `when`(networkService!!.searchImages(apiKey = "abc", querySearch = "Movie", pageNumber = 1))
                .thenReturn(serverCall)

        `when`(serverCall!!.enqueue(any())).then {
            (it.arguments[0] as Callback<MovieResults>)
                    .onResponse(null, response)
        }

        `when`(response!!.code()).thenReturn(404)
        `when`(response!!.isSuccessful).thenReturn(true)

        repository = Repository(networkService!!)
        repository!!.getServerResponse("MOVIE", 1)


        Assert.assertEquals(repository!!.mutableLiveData.value, null)
    }


    @Before
    fun setUp() {
        PowerMockito.mockStatic(Response::class.java)
        MockitoAnnotations.initMocks(this)
    }
}