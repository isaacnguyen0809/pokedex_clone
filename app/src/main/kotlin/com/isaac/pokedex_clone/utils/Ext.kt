package com.isaac.pokedex_clone.utils

import android.content.res.Resources.getSystem
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.isaac.pokedex_clone.R

val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

fun ImageView.loadImageUrl(url: String?, requestListener: RequestListener<Drawable>? = null) {
    Glide.with(context)
        .load(url)
        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        .error(R.drawable.ic_home)
        .listener(requestListener)
        .into(this)
}