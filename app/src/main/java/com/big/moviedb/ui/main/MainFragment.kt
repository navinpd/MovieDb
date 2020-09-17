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
import com.big.moviedb.ui.adapter.SelectableViewAdapter
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainFragment : Fragment() , GetNextItems {

    @Inject
    lateinit var mViewModel: HomeViewModel

    @Inject
    lateinit var glide: RequestManager

    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchViewAdapter: SelectableViewAdapter

    private var currentQuery: String = ""

    private var pageNumber: Int = 1
    private var totalPageCount: Int = 1

    private var listOfMovieResult = mutableListOf<MovieDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setUpView(root)
        val textView: TextView = root.findViewById(R.id.text_home)
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText


        searchPlate.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (v.text.toString().isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE

                    listOfMovieResult.clear()
                    pageNumber = 1

                    currentQuery = v.text.toString()

                    mViewModel.getSearchResult(v.text.toString(), pageNumber)

                    //hide keyboard after click
                    val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchPlate.windowToken, 0)
                }
            }
            false
        }

        mViewModel.getSearchResults.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE

            if (it != null && it.results.isNotEmpty()) {
                listOfMovieResult.addAll(it.results)
                searchViewAdapter.notifyDataSetChanged()
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




    private fun setUpView(view: View) {

        searchView = view.findViewById(R.id.search_sv)
        progressBar = view.findViewById(R.id.progress_circle)

        searchViewAdapter = SelectableViewAdapter(listOfMovieResult, glide)
        searchViewAdapter.requestForNextItem = this

        val recyclerView: RecyclerView = view.findViewById(R.id.search_view_rv)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.adapter = searchViewAdapter
    }

    override fun callForNext() {
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
interface GetNextItems {
    fun callForNext()
}