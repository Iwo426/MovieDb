package com.mobimovie.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobimovie.R
import com.mobimovie.databinding.FragmentDetailBindingImpl
import com.mobimovie.response.MovieDetailResponse
import com.mobimovie.response.UpComingResponse
import com.mobimovie.utils.DataState
import com.mobimovie.utils.loadImage
import com.mobimovie.utils.visible
import com.mobimovie.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    var movieId = 0
    private lateinit var viewModelDetail: MovieDetailViewModel
    lateinit var binding: FragmentDetailBindingImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = DetailFragmentArgs.fromBundle(it).id
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragmen
        binding.callback = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelDetail = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        initUiData()
        binding.callback
    }

    private fun initUiData() {
        viewModelDetail.fetchMovieDetails(movieId)
        viewModelDetail.data.observe(viewLifecycleOwner, Observer {
            it.let {

                when (it) {
                    is DataState.Success<MovieDetailResponse> -> {
                        displayProgressBar(false)
                        binding.data = it.data
                        imgDetail.loadImage("https://image.tmdb.org/t/p/w500/" + it.data.backdrop_path)
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                    }
                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                }

            }
        })
    }

    fun onImageClick(id: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/$id"))
        startActivity(browserIntent)
    }
    private fun displayProgressBar(isDisplayed: Boolean) {
        progressDetail?.visible(isDisplayed)
    }
}