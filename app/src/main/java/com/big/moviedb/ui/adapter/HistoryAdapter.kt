package com.big.moviedb.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.big.moviedb.R

class HistoryAdapter(private val searchedItems: MutableList<String>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var onClickListener: View.OnClickListener? = null

    companion object {
        val TAG = this.javaClass.simpleName
    }

    override fun getItemCount() = searchedItems.size

    fun updateMovieList(newCities: MutableList<String>) {
        searchedItems.clear()
        searchedItems.addAll(newCities)
        Log.d(TAG, "$searchedItems")

        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        onClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(searchedItems[position])
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val searchItem: TextView = itemView.findViewById(R.id.selectable_text)

        fun bind(movieName: String) {
            itemView.tag = movieName

            itemView.setOnClickListener(onClickListener)
            this.searchItem.text = movieName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_selectable_text, parent, false)
    )

}