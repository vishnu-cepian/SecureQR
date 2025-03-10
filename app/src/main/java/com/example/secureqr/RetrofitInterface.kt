package com.example.secureqr

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class UrlRequest(val url: String)
data class ApiResponse(val prediction: String)

interface ApiService {
//    @Headers("Content-Type: application/json")
    @POST("/predict")  // Change this to your actual Flask API endpoint
    fun getPrediction(@Body request: UrlRequest): Call<ApiResponse>
}