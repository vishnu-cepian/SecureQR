package com.example.secureqr

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val DL_BASE_URL = "http://192.168.37.96:5000"  // Replace with your API URL
    private const val IPFS_BASE_URL = "https://gateway.pinata.cloud/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(DL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            retrofit.create(ApiService::class.java)
    }

    val ipfsApi: IpfsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(IPFS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(IpfsApi::class.java)
    }
}