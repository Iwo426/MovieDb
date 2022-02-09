package com.mobimovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobimovie.databinding.SearchItemLayoutBinding
import com.mobimovie.model.NowPlayingModel

class SearchListAdapter :
    PagingDataAdapter<NowPlayingModel, SearchListAdapter.MovieViewHolder>(MoviesComparator) {

    private var listener: ItemInterface? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        return MovieViewHolder(
            SearchItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindPassenger(it) }
    }

    inner class MovieViewHolder(private val binding: SearchItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPassenger(item: NowPlayingModel) = with(binding) {

            //strings'e ekle
            txtSearchResult.text = item.title + " "  + item.release_date
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