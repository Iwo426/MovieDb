package com.mobimovie.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobimovie.R
import com.mobimovie.databinding.FragmentDetailBinding
import com.mobimovie.request.AddToFavoriteRequest
import com.mobimovie.request.AddToWatchListRequest
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.response.CommonResponse
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.response.MovieDetailResponse
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.TYPE_FAV
import com.mobimovie.utils.MobiMovieConstants.TYPE_WATCH
import com.mobimovie.utils.loadImage
import com.mobimovie.utils.showAlert
import com.mobimovie.utils.visible
import com.mobimovie.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    var movieId = 0
    private lateinit var viewModelDetail: MovieDetailViewModel
    private val viewModelAddToFavorite: AddFavoriteViewModel by viewModels()
    private val viewModelAddWatchList: AddWatchListViewModel by viewModels()
    private val accountModel: AccountDetailViewModel by activityViewModels()
    private val sessionViewModel: SessionViewModel by activityViewModels()
    lateinit var binding: FragmentDetailBinding
    var sessionId = ""
    private var userId = 0
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
        // Inflate the layout for this fragment
        binding.callback = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelDetail = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        val pref = activity?.getSharedPreferences(
            "login", Context.MODE_PRIVATE
        )
         sessionId = pref?.getString("sessionId", "0").toString()
         userId = pref?.getInt("id", 0)!!
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
                        binding.imgDetail.loadImage("https://image.tmdb.org/t/p/w500/" + it.data.backdrop_path)
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

    private fun addToFavorite(request: AddToFavoriteRequest) {
        viewModelAddToFavorite.addToFavorite(userId, sessionId, request)
        viewModelAddToFavorite.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<CommonResponse> -> {
                    displayProgressBar(false)
                    showAlert(it.data.status_code, requireContext())
                    viewModelAddToFavorite.data.removeObservers(viewLifecycleOwner)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })

    }

    private fun addToWatchlist(request: AddToWatchListRequest) {
        viewModelAddWatchList.addToWatchlist(userId, sessionId, request)
        viewModelAddWatchList.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<CommonResponse> -> {
                    displayProgressBar(false)
                    showAlert(it.data.status_code, requireContext())
                    viewModelAddWatchList.data.removeObservers(viewLifecycleOwner)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })

    }

     fun getUserDetails(type: Int) {
         if (type == TYPE_FAV) {
             addToFavorite(AddToFavoriteRequest(true, movieId, "movie"))
         } else {
             addToWatchlist(AddToWatchListRequest(true, movieId, "movie"))
         }
    }

    fun onImageClick(id: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/$id"))
        startActivity(browserIntent)
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressDetail.visible(isDisplayed)
    }
}