package com.mobimovie.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.accompanist.coil.rememberCoilPainter
import com.mobimovie.R
import com.mobimovie.adapter.MovieListAdapter
import com.mobimovie.databinding.MovieListFragmentBinding
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.response.*
import com.mobimovie.utils.DataState
import com.mobimovie.utils.loadImage
import com.mobimovie.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoritesListFragment : Fragment(), MovieListAdapter.ItemInterface {

    private var _binding: MovieListFragmentBinding? = null
    private val binding get() = _binding
    private var userId = 0
    private var sessionId = ""

    @ExperimentalCoroutinesApi
    private val viewModelGetFavoriteList: GetFavoriteListViewModel by viewModels()
    lateinit var adapter: MovieListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val pref = activity?.getSharedPreferences(
                    "login", Context.MODE_PRIVATE
                )
                val sessionId = pref?.getString("sessionId", "0").toString()
                val userId = pref?.getInt("id", 0)
               if (userId != null) {
                    LoadUiWithData(userId, sessionId)
                }
            }
        }

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
                    // setUpList(userId, sessionId)
                }
                else -> {}
            }
        })
        return sessionId
    }

    @ExperimentalCoroutinesApi
    private fun setUpList(userId: Int, sessionId: String) {
        lifecycleScope.launch {
            viewModelGetFavoriteList.getFavoriteList(userId, 1, sessionId)
                .collectLatest { pagedData ->
                    adapter.submitData(pagedData)
                }
        }
    }

    override fun sendData(id: Int) {
        val action = FavoritesListFragmentDirections.actionFavoritesListFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    @Composable
    fun LoadUiWithData(userId: Int, sessionId: String) {
        val viewModel: FavoriteListViewModel = viewModel()
        val rememberedList: MutableState<List<NowPlayingModel>> =
            remember { mutableStateOf(emptyList()) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(key1 = "GET_LIST") {
            coroutineScope.launch(Dispatchers.IO) {
                val list = viewModel.getFavList(userId, 1, sessionId)
                rememberedList.value = list.results
            }
        }
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn() {
                items(rememberedList.value) { dataList ->
                    ItemUi(dataList)
                }
            }
        }

    }

    @Composable
    fun ItemUi(model: NowPlayingModel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top)
                .padding(16.dp), elevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                LoadImage(model.poster_path)
                Content(model.title,model.overview,model.release_date)
            }

        }
    }

    @Composable
    fun LoadImage(imageUrl: String) {
        Card(
            shape = RectangleShape,
            modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 16.dp),
            border = BorderStroke(2.dp, color = Color.Cyan),
            elevation = 4.dp
        ) {
            Image(painter = rememberCoilPainter(request = "https://image.tmdb.org/t/p/w500/$imageUrl"), contentDescription = "image")
        }

    }

    @Composable
    fun Content(title:String,desc :String,date :String) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(text = title, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(desc, maxLines = 2)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(text = date)
            }
        }


    }
}