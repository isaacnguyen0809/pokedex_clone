package com.isaac.pokedex_clone.utils

import android.content.res.Resources.getSystem
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.data.remote.retrofit.StatusCode

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

fun Int.getStatusCode(): StatusCode {
    return StatusCode.entries.find { it.code == this }
        ?: StatusCode.Unknown
}

suspend fun <T : Any> ApiResponse<T>.onSuccess(
    executable: suspend (T) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResponse<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Error<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> ApiResponse<T>.onException(
    executable: suspend (e: Throwable) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Exception<T>) {
        executable(throwable)
    }
}