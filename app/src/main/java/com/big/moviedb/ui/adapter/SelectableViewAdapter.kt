package com.big.moviedb.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.big.moviedb.R
import com.big.moviedb.data.remote.response.MovieDetails
import com.big.moviedb.ui.main.GetNextItems
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

class SelectableViewAdapter(
        private var movieListItem: MutableList<MovieDetails>,
        private var glide: RequestManager) : RecyclerView.Adapter<SelectableViewAdapter.ImageViewHolder>() {

    var onClickListener: View.OnClickListener? = null
    private val posterUrl = "https://image.tmdb.org/t/p/w200/"

    override fun getItemCount() = movieListItem.size
    lateinit var requestForNextItem: GetNextItems

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(movieListItem[position], position)
        if (movieListItem.isNotEmpty() && position == movieListItem.size - 1) {
            requestForNextItem.callForNext()
        }
    }

    inner class ImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val selectableImage: ImageView = view.findViewById(R.id.poster_image)
        private val progressCircle: ProgressBar = view.findViewById(R.id.progress_circle)
        private val releaseDate: TextView = view.findViewById(R.id.release_date)
        private val title: TextView = view.findViewById(R.id.title)
        private val overview: TextView = view.findViewById(R.id.overview)
        private val errorImageUrl: TextView = view.findViewById(R.id.error_image_url)

        fun bind(movieDetails: MovieDetails, position: Int) {

            itemView.tag = position
            itemView.setOnClickListener(onClickListener)

            releaseDate.text = "Release Date: " + movieDetails.release_date
            title.text = "Title: " + movieDetails.title
            overview.text = "OverView: " + movieDetails.overview
            errorImageUrl.text = posterUrl + movieDetails.poster_path

            glide.load(errorImageUrl.text.toString())
                    .centerCrop()
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .placeholder(R.drawable.ic_cloud_download_black_24dp)
                    .addListener(object : RequestListener<Drawable?> {

                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            progressCircle.visibility = View.GONE
                            errorImageUrl.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            progressCircle.visibility = View.GONE
                            errorImageUrl.visibility = View.GONE
                            return false
                        }
                    })
                    .into(selectableImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_poster_card, parent, false)
    )

}