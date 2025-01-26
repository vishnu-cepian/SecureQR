package com.example.secureqr

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

fun checkDomainReputation(domain: String, callback: (String) -> Unit) {
    println("inside checkDomainReputation")
    val client = OkHttpClient()
//    val url = "https://api.threatintelligenceplatform.com/v1/reputation?apiKey=$apiKey&domain=$domain"
//    val url =
//        "https://api.threatintelligenceplatform.com/v1/malwareCheck?domainName=$domain&apiKey=at_RzidTDi4kuRpLBBmik9BUtAwzjMwA"
    val url = "https://api.threatintelligenceplatform.com/v1/reputation?domainName=$domain&mode=fast&apiKey=at_RzidTDi4kuRpLBBmik9BUtAwzjMwA"
    val request = Request.Builder()
        .url(url)
        .build()

    Thread {
        try {
            val response = client.newCall(request).execute()
            println("response(API) : $response")
            val responseBody = response.body?.string()
            if (responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                println("jsonResponse(API) : $jsonResponse")
                if (jsonResponse.has("reputationScore")) {
                    val reputationScore = jsonResponse.getInt("reputationScore")
                    callback("Reputation Score: $reputationScore (0 = dangerous, 100 = safe)")
                } else {
                    val errorMessage = jsonResponse.optString("message", "Unknown error")
                    callback("No reputationScore field. Message: $errorMessage")
                }
            } else {
                callback("Empty response from API")
            }
        } catch (e: Exception) {
            callback("Exception: ${e.message}")
        }
    }.start()
}