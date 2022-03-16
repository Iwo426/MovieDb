package com.mobimovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobimovie.databinding.SliderLayoutBinding
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.utils.loadImage
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val sliderDataArrayList: List<NowPlayingModel>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {

    override fun onBindViewHolder(holder: SliderAdapterViewHolder, position: Int) {
        with(holder) {
            with(sliderDataArrayList[position]) {
                binding.tvTitle.text = title
                binding.tvDesc.text = overview
                binding.sliderImage.loadImage("https://image.tmdb.org/t/p/original/$backdrop_path")
            }
        }
    }

    class SliderAdapterViewHolder(val binding: SliderLayoutBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
    }

    override fun getCount(): Int {
        return sliderDataArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val binding = SliderLayoutBinding
            .inflate(LayoutInflater.from(parent?.context), parent, false)
        return SliderAdapterViewHolder(binding)
    }

}