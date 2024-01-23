package com.isaac.pokedex_clone.data.remote.retrofit

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

internal class ApiResponseCallAdapter(
    private val resultType: Type,
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCall(call)
    }
}

class ApiResponseCall<T>(
    private val proxy: Call<T>,
) : Call<ApiResponse<T>> {

    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        return proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                response.body()?.let {
                    when (val responseCode = response.code()) {
                        in 200..208 -> {
                            callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.Success(it)))
                        }

                        else ->
                            callback.onResponse(
                                this@ApiResponseCall,
                                Response.success(ApiResponse.Error(responseCode, response.message())),
                            )
                    }
                } ?: callback.onResponse(
                    this@ApiResponseCall,
                    Response.success(ApiResponse.Exception(Exception("Response body is null")))
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.Exception(t)))
            }

        })
    }

    override fun clone(): Call<ApiResponse<T>> = ApiResponseCall(proxy.clone())

    override fun execute(): Response<ApiResponse<T>> = throw NotImplementedError()

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun cancel() = proxy.cancel()

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

}
