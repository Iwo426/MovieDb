package com.mobimovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobimovie.adapter.MovieListAdapter
import com.mobimovie.databinding.MovieListFragmentBinding
import com.mobimovie.response.*
import com.mobimovie.utils.DataState
import com.mobimovie.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WatchListFragment : Fragment(), MovieListAdapter.ItemInterface {

    private var _binding: MovieListFragmentBinding? = null
    private val binding get() = _binding
    private var userId = 0

    @ExperimentalCoroutinesApi
    private val viewModelGetWatchList: GetWatchListViewModel by viewModels()
    lateinit var adapter: MovieListAdapter
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
        _binding = MovieListFragmentBinding.inflate(inflater, container, false)
        getUserId()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWatchList()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        // binding?.progressBar?.visible(isDisplayed)
    }

    private fun setWatchList() {
        adapter = MovieListAdapter()
        adapter.interfaceListener(this)
        binding?.movieList?.layoutManager = LinearLayoutManager(context)
        binding?.movieList?.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding?.movieList?.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun getUserId(): Int {
        val accountModel: AccountDetailViewModel by activityViewModels()
        accountModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<AccountDetailResponse> -> {
                    userId = it.data.id
                    getSession()
                }
                else -> {}
            }
        })
        return userId
    }

    @ExperimentalCoroutinesApi
    private fun getSession(): String {
        lateinit var sessionId: String
        val sessionViewModel: SessionViewModel by activityViewModels()
        sessionViewModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<CreateSessionIdResponse> -> {
                    sessionId = it.data.session_id
                    setUpList(userId, sessionId)
                }
                else -> {}
            }
        })
        return sessionId
    }

    @ExperimentalCoroutinesApi
    private fun setUpList(userId: Int, sessionId: String) {
        lifecycleScope.launch {
            viewModelGetWatchList.getWatchList(userId, 1, sessionId)
                .collectLatest { pagedData ->
                    adapter.submitData(pagedData)
                }
        }
    }

    override fun sendData(id: Int) {
        val action = WatchListFragmentDirections.actionWatchListFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }
}