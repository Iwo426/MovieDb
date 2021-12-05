package com.mobimovie.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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

