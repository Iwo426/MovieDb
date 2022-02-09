package com.mobimovie.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mobimovie.R
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.utils.DataState
import com.mobimovie.viewmodel.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val sessionViewModel: SessionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.fragment)
        bottomnavigationbar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment) {
                bottomnavigationbar.visibility = View.GONE
            } else {
                bottomnavigationbar.visibility = View.VISIBLE
            }
        }
    }
}