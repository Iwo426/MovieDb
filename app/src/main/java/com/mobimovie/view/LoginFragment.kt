package com.mobimovie.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mobimovie.R
import com.mobimovie.databinding.FragmentLoginBinding
import com.mobimovie.request.LoginRequest
import com.mobimovie.request.SessionRequest
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.utils.DataState
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
    var navigationState = false

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
                SetUIScreen()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestToken()
        navigationState = false
        /*   binding?.btnlogin?.setOnClickListener {
               val userId = binding?.txtUserId?.text.toString()
               val pw = binding?.txtPw?.text.toString()

               if (isEmpty(userId) && isEmpty(pw)) {
                   Toast.makeText(context, getString(R.string.emptyStringWarning), Toast.LENGTH_SHORT)
                       .show()
               } else {
                   val request = LoginRequest(userId,pw,token)
                   logIn(request)
               }

           }*/

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
                        Toast.makeText(
                            context,
                            "Kullanıcı/Şifre Bilgilerinizi Kontrol Ediniz",
                            Toast.LENGTH_SHORT
                        ).show()
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
                        val sharedPref =
                            activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
                                ?: return@Observer
                        with(sharedPref.edit()) {
                            putString("sessionId", it.data.session_id.replace("[\"]".toRegex(), ""))
                            apply()
                        }
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
                        if (!navigationState) {
                            navigateToHome()
                            navigationState = true
                            val sharedPref =
                                activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
                                    ?: return@Observer
                            with(sharedPref.edit()) {
                                putInt("id", it.data.id)
                                apply()
                            }
                        }

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

    @Composable
    fun SetUIScreen() {
        Surface {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                var state by remember { mutableStateOf(false) }
                var userId by remember { mutableStateOf("") }
                var password by rememberSaveable { mutableStateOf("") }

                Image(
                    painter = painterResource(R.drawable.moviedb_logo),
                    contentDescription = "logo",
                    modifier = Modifier.padding(0.dp,32.dp,0.dp,32.dp)
                )
                SimpleOutlinedTextFieldSample(userId) { txt -> userId = txt }
                Spacer(modifier = Modifier.height(16.dp))
                PasswordTextField(password) { pw -> password = pw }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {

                        if (isEmpty(userId) && isEmpty(password)) {
                            Toast.makeText(context, getString(R.string.emptyStringWarning), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val request = LoginRequest(userId,password,token)
                            logIn(request)
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(getString(R.string.giris_yap), color = Color.White)

                }
            }
        }
    }

    @Composable
    fun PasswordTextField(password : String , passwordUpdate :(pw :String)-> Unit) {

        TextField(
            value = password,
            onValueChange = passwordUpdate,
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 0.dp, 32.dp, 0.dp)
        )
    }

    @Composable
    fun SimpleOutlinedTextFieldSample(text : String ,textUpdate :(pw :String)-> Unit) {

        OutlinedTextField(
            value = text,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 0.dp, 32.dp, 0.dp),
            onValueChange = textUpdate,
            label = { Text("Kullanıcı Adı") }
        )
    }

}