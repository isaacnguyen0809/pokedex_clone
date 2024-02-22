package com.isaac.pokedex_clone.data.remote.retrofit

sealed interface ApiResponse<out T> {

    class Success<T>(val data: T) : ApiResponse<T>

    class Error<T>(val code: Int, val message: String?, val error: ErrorResponse) : ApiResponse<T>

    class Exception<T>(val throwable: Throwable) : ApiResponse<T>

}


