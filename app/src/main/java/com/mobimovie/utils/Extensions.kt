package com.mobimovie.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobimovie.R
import com.mobimovie.utils.MobiMovieConstants.addStatus

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

fun ImageView.loadImageWithResize(url: String) {
    Glide.with(this)
        .load(url)
        .apply( RequestOptions().override(350, 800))
        .into(this)
}

fun isEmpty(text: String): Boolean {
   return text.isEmpty()
}

fun showAlert(status :Int,context : Context){
    val dialog  =  Dialog(context)
    dialog.setContentView(R.layout.pop_up_layout)
    val btnConfirm: Button = dialog.findViewById<View>(R.id.btnConfirm) as Button
    val img: ImageView = dialog.findViewById<View>(R.id.statusImage) as ImageView
    val txt: TextView = dialog.findViewById<View>(R.id.statusText) as TextView
    if (status == addStatus){
        img.setImageResource(R.drawable.thumbs)
        txt.text = context.getString(R.string.success)
    }else {
        img.setImageResource(R.drawable.sad)
        txt.text = context.getString(R.string.fail)
    }
    btnConfirm.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

