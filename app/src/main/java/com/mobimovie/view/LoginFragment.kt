package com.mobimovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.gson.JsonObject
import com.mobimovie.R
import com.mobimovie.databinding.FragmentLoginBinding
import com.mobimovie.request.LoginRequest
import com.mobimovie.request.SessionRequest
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import com.mobimovie.utils.isEmpty
import com.mobimovie.utils.visible
import com.mobimovie.viewmodel.AccountDetailViewModel
import com.mobimovie.viewmodel.LoginViewModel
import com.mobimovie.viewmodel.RequestTokenViewModel
import com.mobimovie.viewmodel.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val viewModelRequestToken: RequestTokenViewModel by viewModels()
    private val viewModelLogin: LoginViewModel by viewModels()
    private val viewModelSessionViewModel: SessionViewModel by activityViewModels()
    private val viewModelAccountDetailViewModel: AccountDetailViewModel by activityViewModels()
    private var token: String = ""

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestToken()
        binding?.btnlogin?.setOnClickListener {
            val userId = binding?.txtUserId?.text.toString()
            val pw = binding?.txtPw?.text.toString()

            if (isEmpty(userId) && isEmpty(pw)) {
                Toast.makeText(context, getString(R.string.emptyStringWarning), Toast.LENGTH_SHORT)
                    .show()
            } else {
                val request = LoginRequest(userId,pw,token)
                logIn(request)
            }

        }

    }

    private fun requestToken() {
        viewModelRequestToken.getToken(API_KEY)
        viewModelRequestToken.data.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    is DataState.Success<RequestTokenResponse> -> {
                        displayProgressBar(false)
                        token = it.data.request_token
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

    private fun logIn(request: LoginRequest) {
        viewModelLogin.login(request)
        viewModelLogin.data.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    is DataState.Success<RequestTokenResponse> -> {
                        displayProgressBar(false)
                        getSessionId(SessionRequest(it.data.request_token))
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

    private fun getSessionId(request: SessionRequest) {

        viewModelSessionViewModel.getSessionId(API_KEY, request)
        viewModelSessionViewModel.data.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    is DataState.Success<CreateSessionIdResponse> -> {
                        displayProgressBar(false)
                        viewModelAccountDetailViewModel.getAccountDetail(
                            API_KEY,
                            it.data.session_id
                        )
                        getDetail()
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

    private fun getDetail() {
        viewModelAccountDetailViewModel.data.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    is DataState.Success<AccountDetailResponse> -> {
                        displayProgressBar(false)
                        navigateToHome()
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

    private fun displayProgressBar(isDisplayed: Boolean) {
         binding?.progressBarLogin?.visible(isDisplayed)
    }

    private fun navigateToHome() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        view?.let { Navigation.findNavController(it).navigate(action) }
    }
}