package com.mobimovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobimovie.utils.loadImage
import com.mobimovie.R
import com.mobimovie.model.NowPlayingModel
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.slider_layout.view.*

class SliderAdapter(sliderDataArrayList: List<NowPlayingModel>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {
    private val mSliderItems: List<NowPlayingModel> = sliderDataArrayList

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.slider_layout, parent, false)
        return SliderAdapterViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        val sliderItem: NowPlayingModel = mSliderItems[position]

        viewHolder.view.tvTitle.text = sliderItem.title
        viewHolder.view.tvDesc.text = sliderItem.overview
        viewHolder.view.sliderImage.loadImage("https://image.tmdb.org/t/p/original/" + sliderItem.backdrop_path)
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    class SliderAdapterViewHolder(var view: View) : ViewHolder(view) {

    }

}