package com.mobimovie.view

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
import com.mobimovie.databinding.FragmentDetailBindingImpl
import com.mobimovie.request.AddToFavoriteRequest
import com.mobimovie.request.AddToWatchListRequest
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.response.CommonResponse
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.response.MovieDetailResponse
import com.mobimovie.utils.DataState
import com.mobimovie.utils.loadImage
import com.mobimovie.utils.showAlert
import com.mobimovie.utils.visible
import com.mobimovie.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    var movieId = 0
    private lateinit var viewModelDetail: MovieDetailViewModel
    private val viewModelAddToFavorite: AddFavoriteViewModel by viewModels()
    private val viewModelAddWatchList: AddWatchListViewModel by viewModels()
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
        btnAddFavorite.setOnClickListener {
            addToFavorite(AddToFavoriteRequest(true, movieId, "movie"))
        }

        btnAddWatchList.setOnClickListener {
            addToWatchlist(AddToWatchListRequest(true, movieId, "movie"))
        }
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

    private fun addToFavorite(request: AddToFavoriteRequest) {
        viewModelAddToFavorite.addToFavorite(getUserId(), getSession(), request)
        viewModelAddToFavorite.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<CommonResponse> -> {
                    displayProgressBar(false)
                    showAlert(it.data.status_code, requireContext())
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
        viewModelAddWatchList.addToWatchlist(getUserId(), getSession(), request)
        viewModelAddWatchList.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<CommonResponse> -> {
                    displayProgressBar(false)
                    showAlert(it.data.status_code, requireContext())
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

    private fun getUserId(): Int {
        var userId = 0
        val accountModel: AccountDetailViewModel by activityViewModels()
        accountModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<AccountDetailResponse> -> {
                    userId = it.data.id
                }
                else -> {}
            }
        })
        return userId
    }

    private fun getSession(): String {
        lateinit var sessionId: String
        val sessionViewModel: SessionViewModel by activityViewModels()
        sessionViewModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<CreateSessionIdResponse> -> {
                    sessionId = it.data.session_id
                }
                else -> {}
            }
        })
        return sessionId
    }

    fun onImageClick(id: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/$id"))
        startActivity(browserIntent)
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        progressDetail?.visible(isDisplayed)
    }
}