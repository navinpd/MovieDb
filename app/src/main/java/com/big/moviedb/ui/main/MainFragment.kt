package com.big.moviedb.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.big.moviedb.MyApplication
import com.big.moviedb.R
import com.big.moviedb.data.remote.response.MovieDetails
import com.big.moviedb.di.component.DaggerFragmentComponent
import com.big.moviedb.di.module.MainFragmentModule
import com.big.moviedb.ui.adapter.HistoryAdapter
import com.big.moviedb.ui.adapter.MovieAdapter
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class MainFragment : Fragment(), INextPage {

    @Inject
    lateinit var mViewModel: HomeViewModel

    @Inject
    lateinit var glide: RequestManager

    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var textView: TextView
    private lateinit var searchPlate: EditText

    private lateinit var movieRV: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private var currentQuery: String = ""

    private var pageNumber: Int = 1

    private var totalPageCount: Int = 1

    private var searchedItems: ArrayList<String> = ArrayList()
    private var listOfMovieResult = mutableListOf<MovieDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setUpView(root)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            searchedItems.clear()
            if (hasFocus) {
                val set = mViewModel.getListFromLocal()
                if (set != null && set.isNotEmpty()) {
                    historyAdapter.updateMovieList(ArrayList(set))
                }
            }
        }

        searchPlate.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (v.text.toString().isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE

                    listOfMovieResult.clear()
                    pageNumber = 1

                    currentQuery = v.text.toString()

                    mViewModel.getSearchResult(v.text.toString(), pageNumber)

                    hideKeyboard()
                }
            }
            false
        }

        mViewModel.getSearchResults.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE

            if (it != null && it.results.isNotEmpty()) {
                listOfMovieResult.addAll(it.results)
                movieAdapter.notifyDataSetChanged()
                textView.visibility = View.GONE
                totalPageCount = it.total_pages
                pageNumber = it.page

            } else if (it != null && it.results.isEmpty() && root != null) {

                Snackbar.make(root, "No Movie Results Found", Snackbar.LENGTH_LONG).show()
            } else if (root != null) {

                Snackbar.make(root, "Network Error", Snackbar.LENGTH_LONG).show()
            }
        })

        return root
    }

    private fun hideKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchPlate.windowToken, 0)
    }

    private fun setUpView(view: View) {
        searchedItems = ArrayList()

        textView = view.findViewById(R.id.text_home)
        searchView = view.findViewById(R.id.search_sv)
        progressBar = view.findViewById(R.id.progress_circle)
        searchPlate = searchView.findViewById(R.id.search_src_text)

        movieRV = view.findViewById(R.id.movie_item_list)
        movieRV.layoutManager = GridLayoutManager(context, 1)
        movieAdapter = MovieAdapter(listOfMovieResult, glide)
        movieRV.adapter = movieAdapter
        movieAdapter.requestForNextItem = this

        historyRecyclerView = view.findViewById(R.id.history_item_list)
        historyRecyclerView.layoutManager = GridLayoutManager(context, 1)
        historyAdapter = HistoryAdapter(searchedItems)
        historyRecyclerView.adapter = historyAdapter

        historyAdapter.setOnItemClickListener(View.OnClickListener {
            val selectedMovie = view.tag as String
            Toast.makeText(this.context, selectedMovie, Toast.LENGTH_SHORT).show()
            searchView.setQuery("selectedMovie", true)
            hideKeyboard()
        })
    }

    override fun loadNextPage() {
        if (pageNumber < totalPageCount) {
            pageNumber++
            progressBar.visibility = View.VISIBLE
            mViewModel.getSearchResult(
                    currentQuery, pageNumber
            )
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }


    private fun getDependencies() {
        DaggerFragmentComponent
                .builder()
                .applicationComponent(
                        (context!!
                                .applicationContext as MyApplication).applicationComponent
                )
                .mainFragmentModule(MainFragmentModule(this))
                .build()
                .inject(this)
    }

}

//Interface to invoke query for next set of Images
interface INextPage {
    fun loadNextPage()
}