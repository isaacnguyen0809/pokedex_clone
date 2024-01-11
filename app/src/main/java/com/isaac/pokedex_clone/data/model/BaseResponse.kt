package com.isaac.pokedex_clone.data.model

sealed class BaseResponse<out T> {
    data object Loading: BaseResponse<Nothing>()

    data class Success<out T>(
        val data: T
    ): BaseResponse<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int,
    ): BaseResponse<Nothing>()
}