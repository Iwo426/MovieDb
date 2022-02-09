package com.mobimovie.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobimovie.R
import com.mobimovie.adapter.MovieListAdapter
import com.mobimovie.adapter.SearchListAdapter
import com.mobimovie.databinding.FragmentLoginBinding
import com.mobimovie.databinding.SearchFragmentBinding
import com.mobimovie.utils.visible
import com.mobimovie.viewmodel.GetFavoriteListViewModel
import com.mobimovie.viewmodel.SearchListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController


@AndroidEntryPoint
class SearchFragment : Fragment(), SearchListAdapter.ItemInterface {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding

    @ExperimentalCoroutinesApi
    private val viewModelSearchList: SearchListViewModel by viewModels()
    lateinit var adapter: SearchListAdapter
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
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList()
        binding?.mSearch?.isIconified = false
        binding?.mSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //Performs search when user hit the search button on the keyboard
                if (p0 != null) {
                    setUpList(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Start filtering the list as user start entering the characters
                return false
            }
        })
    }

    private fun setList() {
        adapter = SearchListAdapter()
        adapter.interfaceListener(this)
        binding?.searchList?.layoutManager = LinearLayoutManager(context)
        binding?.searchList?.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding?.searchList?.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun setUpList(query: String) {
        lifecycleScope.launch {
            viewModelSearchList.searchMovie(1, query)
                .collectLatest { pagedData ->
                    adapter.submitData(pagedData)
                }
        }
    }

    override fun sendData(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

}
