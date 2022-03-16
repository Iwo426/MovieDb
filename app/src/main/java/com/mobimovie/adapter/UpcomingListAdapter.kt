package com.mobimovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobimovie.databinding.ItemMovieListBinding
import com.mobimovie.model.UpcomingModel
import com.mobimovie.utils.loadImageWithResize

class UpcomingListAdapter(private val listAll: ArrayList<UpcomingModel>) :
    RecyclerView.Adapter<UpcomingListAdapter.ListItemHolder>() {

    private var listener: ItemInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val binding = ItemMovieListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemHolder(binding)
    }

    inner class ListItemHolder(val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun getItemCount(): Int {
        return listAll.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {

        with(holder) {
            with(listAll[position]) {
                binding.txtTitle.text = title
                binding.txtDescription.text = overview
                binding.txtDate.text = release_date
                binding.poster.loadImageWithResize("https://image.tmdb.org/t/p/w500/$poster_path")

                itemView.setOnClickListener {
                    listAll[position].id.let {
                        listener?.sendData(listAll[position].id)
                    }
                }
            }
        }
    }

    fun updateList(allList: List<UpcomingModel>) {
        listAll.clear()
        listAll.addAll(allList)
        notifyDataSetChanged()
    }

    interface ItemInterface {
        fun sendData(id: Int)
    }

    fun interfaceListener(listener: ItemInterface?) {
        this.listener = listener
    }
}