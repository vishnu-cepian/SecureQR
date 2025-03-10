package com.example.secureqr

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import okhttp3.*

import java.io.IOException

fun checkDomainReputation( urlToScan: String,callback: (String) -> Unit) {
    val apiKey = "06795d20dfddda8774c9e80129c57f2d017eadcd542c226ec9b527ed1804bcec"
    val client = OkHttpClient()

    // VirusTotal API URL for scanning
    val apiUrl = "https://www.virustotal.com/api/v3/urls"

    // Request body (URL needs to be sent as "url" form data)
    val requestBody = FormBody.Builder()
        .add("url", urlToScan)
        .build()

    // Build the request with API key
    val request = Request.Builder()
        .url(apiUrl)
        .post(requestBody)
        .addHeader("x-apikey", apiKey) // Add API key in header
        .build()

    // Execute the request asynchronously
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("Failed to connect: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) {
                    println("Request failed: ${response.code}")
                    return
                }

                // Parse response JSON
                val responseBody = response.body?.string()
                val json = JSONObject(responseBody ?: "{}")

                println("VirusTotal Response: $json")
                val scanId = json.getJSONObject("data").getString("id")
                println("Scan ID: $scanId")
                getScanReport(apiKey,scanId) {malicious ->
                    callback(malicious.toString())
                }
            }
        }
    })
}
fun getScanReport(apiKey: String, scanId: String, callback: (Int) -> Unit) {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://www.virustotal.com/api/v3/analyses/$scanId")
        .get()
        .addHeader("x-apikey", apiKey)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("Failed to fetch report: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                val jsonResponse = JSONObject(response.body?.string() ?: "{}")
                println("Scan Report: $jsonResponse")
                val malicious = jsonResponse.getJSONObject("data").getJSONObject("attributes").getJSONObject("stats").getInt("malicious")

                callback(malicious)
            }
        }
    })
}

