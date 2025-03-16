package com.example.secureqr

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class IpfsResponse(
    val productData: ProductData,
    val qrhash: String
)

data class ProductData(
    val productName: String,
    val serialNumber: String,
    val batch: String,
    val companyName: String
)

interface IpfsApi {
    @GET("ipfs/{cid}")
     fun getProductData(@Path("cid") cid: String): Call<ResponseBody>
}
