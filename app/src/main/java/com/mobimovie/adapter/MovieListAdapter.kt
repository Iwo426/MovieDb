package com.mobimovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobimovie.databinding.ItemMovieListBinding
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.utils.loadImageWithResize

class MovieListAdapter :
    PagingDataAdapter<NowPlayingModel, MovieListAdapter.MovieViewHolder>(MoviesComparator) {

    private var listener: ItemInterface? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindPassenger(it) }
    }

    inner class MovieViewHolder(private val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPassenger(item: NowPlayingModel) = with(binding) {

            txtTitle.text = item.title
            txtDescription.text = item.overview
            txtDate.text = item.release_date
            poster.loadImageWithResize("https://image.tmdb.org/t/p/w500/" + item.poster_path)
            itemView.setOnClickListener {
                listener?.sendData(item.id)
            }
        }
    }

    object MoviesComparator : DiffUtil.ItemCallback<NowPlayingModel>() {
        override fun areItemsTheSame(oldItem: NowPlayingModel, newItem: NowPlayingModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NowPlayingModel,
            newItem: NowPlayingModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface ItemInterface {
        fun sendData(id: Int)
    }

    fun interfaceListener(listener: ItemInterface?) {
        this.listener = listener
    }
}