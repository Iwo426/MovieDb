package com.mobimovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobimovie.R
import com.mobimovie.utils.loadImageWithResize
import com.mobimovie.model.UpcomingModel
import kotlinx.android.synthetic.main.item_movie_list.view.*

class UpcomingListAdapter(private val listAll: ArrayList<UpcomingModel>, val context: Context) :
    RecyclerView.Adapter<UpcomingListAdapter.ListItemHolder>() {

    private var listener: ItemInterface? = null

    class ListItemHolder(var view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_movie_list, parent, false)
        return ListItemHolder(view)

    }

    override fun getItemCount(): Int {
        return listAll.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {

        holder.view.txtTitle.text = listAll[position].title
        holder.view.txtDescription.text = listAll[position].overview
        holder.view.txtDate.text = listAll[position].release_date
        holder.view.poster.loadImageWithResize("https://image.tmdb.org/t/p/w500/" + listAll[position].poster_path)

        holder.view.setOnClickListener {
            listAll[position].id.let {
                listener?.sendData(listAll[position].id)
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