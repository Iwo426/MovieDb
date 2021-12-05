package com.mobimovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.mobimovie.utils.visible
import com.mobimovie.adapter.SliderAdapter
import com.mobimovie.adapter.UpcomingListAdapter
import com.mobimovie.databinding.FragmentHomeBinding
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.model.UpcomingModel
import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.response.UpComingResponse
import com.mobimovie.utils.DataState
import com.mobimovie.viewmodel.NowPlayingViewModel
import com.mobimovie.viewmodel.UpComingViewModel
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : Fragment(), UpcomingListAdapter.ItemInterface {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModelNowPlaying: NowPlayingViewModel by viewModels()
    private val viewModelUpComing: UpComingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.swipe?.setOnRefreshListener {
            initSlider()
            initUpcomingList()
            binding!!.swipe.isRefreshing = false
        }

        binding?.appbar?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                    // Collapsed
                    binding!!.swipe.isEnabled = false
                }
                verticalOffset == 0 -> {
                    // Expanded
                    binding!!.swipe.isEnabled = true
                }
                else -> {
                    // Somewhere in between
                    binding!!.swipe.isEnabled = false
                }
            }
        })

        initSlider()
        initUpcomingList()
    }

    fun onOffsetChanged(appBarLayout: AppBarLayout?, i: Int) {
        binding?.swipe?.isEnabled = i == 0
    }

    private fun initSlider() {
        viewModelNowPlaying.data.observe(viewLifecycleOwner, Observer {
            it.let {

                when (it) {
                    is DataState.Success<NowPlayingResponse> -> {
                        displayProgressBar(false)
                        setNowPlayingData(it.data.results)
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

    private fun initUpcomingList() {
        viewModelUpComing.data.observe(viewLifecycleOwner, Observer {

            when (it) {
                is DataState.Success<UpComingResponse> -> {
                    displayProgressBar(false)
                    setUpcomingData(it.data.results)
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

    override fun sendData(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

    private fun setUpcomingData(results: List<UpcomingModel>) {
        val adapter = UpcomingListAdapter(arrayListOf(), requireContext())
        adapter.interfaceListener(this)
        adapter.updateList(results)
        binding?.rvMovies?.layoutManager = LinearLayoutManager(context)
        binding?.rvMovies?.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding?.rvMovies?.adapter = adapter
    }

    private fun setNowPlayingData(results: List<NowPlayingModel>) {
        val sliderAdapter = SliderAdapter(results)
        binding?.slider?.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        binding?.slider?.setSliderAdapter(sliderAdapter)
        binding?.slider?.scrollTimeInSec = 3
        binding?.slider?.isAutoCycle = true
        binding?.slider?.startAutoCycle()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding?.progressBar?.visible(isDisplayed)
    }
}