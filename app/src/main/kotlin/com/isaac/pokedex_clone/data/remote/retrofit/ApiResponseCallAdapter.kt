package com.isaac.pokedex_clone.data.remote.retrofit

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

internal class ApiResponseCallAdapter(
    private val resultType: Type,
    private val moshi: Moshi,
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCall(call, moshi)
    }
}

class ApiResponseCall<T>(
    private val proxy: Call<T>,
    private val moshi: Moshi,
) : Call<ApiResponse<T>> {

    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        return proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.Success(it)))
                    } ?: callback.onResponse(
                        this@ApiResponseCall,
                        Response.success(ApiResponse.Exception(Exception("Response body is null")))
                    )
//                    val errorResponseString = "{\"error\":\"Co loi xay ra\",\"code\":4044}"
//                    val jsonAdapter: JsonAdapter<ErrorResponse> = moshi.adapter(ErrorResponse::class.java)
//                    val errorResponse = jsonAdapter.fromJson(errorResponseString)
//                    Timber.d("Error response: $errorResponse")
//                    callback.onResponse(
//                        this@ApiResponseCall,
//                        Response.success(
//                            ApiResponse.Error(
//                                response.code(),
//                                response.message(),
//                                errorResponse ?: ErrorResponse("Unknown error", 0)
//                            )
//                        ),
//                    )
                } else {
                    val jsonAdapter: JsonAdapter<ErrorResponse> = moshi.adapter(ErrorResponse::class.java)
                    response.errorBody()
                    val errorResponse = jsonAdapter.fromJson(response.errorBody().toString())
                    callback.onResponse(
                        this@ApiResponseCall,
                        Response.success(
                            ApiResponse.Error(
                                response.code(),
                                response.message(),
                                errorResponse ?: ErrorResponse("Unknown error", 0)
                            )
                        ),
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.Exception(t)))
            }

        })
    }

    override fun clone(): Call<ApiResponse<T>> = ApiResponseCall(proxy.clone(), moshi)

    override fun execute(): Response<ApiResponse<T>> = throw NotImplementedError()

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun cancel() = proxy.cancel()

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

}
