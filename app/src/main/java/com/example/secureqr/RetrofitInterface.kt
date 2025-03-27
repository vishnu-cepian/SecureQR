package com.example.secureqr

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class UrlRequest(val url: String)
data class ApiResponse(val prediction: String)
data class Company( val _id: String,
                    val name: String
)

interface ApiService {
    @POST("/predict")
    fun getPrediction(@Body request: UrlRequest): Call<ApiResponse>

}

interface RetrofitInterface {
    @GET("companies")
    fun getCompanies(): Call<List<Company>>
}