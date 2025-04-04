package com.example.secureqr

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.secureqr.ui.theme.SecureQrTheme
import com.google.zxing.integration.android.IntentIntegrator
import java.security.MessageDigest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.material3.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.json.JSONException

class MainActivity : ComponentActivity() {

    private val blockchainHelper = BlockchainHelper(this)
    private var selectedCompany: String? = null

    private val scanResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, data)

                if (scanResult != null && scanResult.contents != null) {
                    Log.d("QRScan", "Scan result: ${scanResult.contents}")
                    val qrContent = scanResult.contents
                    val qrHash = hashQrContent(qrContent)

                    Log.d("QRScan", "Original Content: $qrContent")
                    Log.d("QRScan", "SHA-256 Hash: $qrHash")
                    val company = selectedCompany

                    println("----- $selectedCompany -----------")
                    if (company != null) {
                        println("------------INSIDE PRODUCT---------------")
                        val intent = Intent(this, ProductResultActivity::class.java)
                        val progressDialog = ProgressDialog(this).apply {
                            setMessage("Verifying Product Authenticity, please wait...")
                            setCancelable(false)
                            show()
                        }
                        try {
                            val jsonObject = JSONObject(qrContent)
                            val ipfsCID = jsonObject.getString("ipfsCID")
                            val hash = jsonObject.getString("qrhash")
                            println(ipfsCID)

                            val productHash = generateSHA256Hash(ipfsCID, hash)
                            println("Generated QR Hash: $productHash")

                            IPFSAPI(ipfsCID) { ipfsData ->
                                if (ipfsData != null) {
                                    val productData = ipfsData.productData
                                    val hashIPFS = ipfsData.qrhash
                                    if (hash == hashIPFS) {
                                        println("qrHash from IPFS is confirmed with qrHash from Scanned QR")

                                        blockchainHelper.isProductAuthentic(company, productHash) { isAuthentic ->
                                            runOnUiThread {
                                                if (isAuthentic) {
                                                    println("Product is authentic for $company")
                                                    Toast.makeText(this, "AUTHENTIC PRODUCT", Toast.LENGTH_SHORT).show()
                                                    intent.putExtra("AUTHENTIC", true)
                                                    intent.putExtra("PRODUCT_NAME", productData.productName)
                                                    intent.putExtra("SERIAL_NUMBER", productData.serialNumber)
                                                    intent.putExtra("BATCH", productData.batch)
                                                    intent.putExtra("COMPANY_NAME", productData.companyName)
                                                    progressDialog.dismiss()
                                                    startActivity(intent)
                                                } else {
                                                    Toast.makeText(this, "COUNTERFEIT PRODUCT", Toast.LENGTH_SHORT).show()
                                                    println("Product is NoT authentic for $company")
                                                    intent.putExtra("AUTHENTIC", false)
                                                    progressDialog.dismiss()
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    println("API fetch failed")
                                    intent.putExtra("AUTHENTIC", false)
                                    progressDialog.dismiss()
                                    startActivity(intent)
                                }
                            }
                        } catch (e: JSONException) {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra("IS_TEXT", "⚠\uFE0F This QR code is not supported for product authentication.")
                            progressDialog.dismiss()
                            startActivity(intent)
                    }
                    } else {
                        when {
                            Patterns.WEB_URL.matcher(qrContent).matches() -> {

                                val progressDialog = ProgressDialog(this).apply {
                                    setMessage("Scanning Qr Code, please wait...")
                                    setCancelable(false)
                                    show()
                                }

                                blockchainHelper.checkIfHashExists(qrHash) { isMalicious ->
                                    runOnUiThread {
                                        val intent = Intent(this, ResultActivity::class.java)
                                        intent.putExtra("SCANNED_RESULT", scanResult.contents)
                                        intent.putExtra("HASHED_CONTENT", qrHash)

                                        if (isMalicious) {
                                            Handler(Looper.getMainLooper()).post {
                                                Toast.makeText(this, "MALICIOUS URL!!!", Toast.LENGTH_LONG).show()
                                            }
                                            intent.putExtra("IS_MALICIOUS", true)
                                            println("This QR code is Malicious.")
                                            progressDialog.dismiss()
                                            startActivity(intent)
                                        } else {

                                            intent.putExtra("BLOCKCHAIN_VERIFIED", true)

                                            getFinalRedirectedUrl(qrContent) { finalUrl ->
                                                val resolvedUrl = finalUrl ?: qrContent
                                                println(resolvedUrl)

                                                checkDomainReputation(resolvedUrl) { result ->
                                                    println(result)
                                                    if (result.toIntOrNull()!! > 2) {
                                                        intent.putExtra("IS_MALICIOUS", true)
                                                        intent.putExtra("API_VERIFIED", 0)  //malicious
                                                        blockchainHelper.addHashToBlockchain(qrHash) { success ->
                                                            if (success) {
                                                                println("Hash added to blockchain successfully.")
                                                                intent.putExtra("BLOCKCHAIN_ADD_VERIFIED", 0)    //success
                                                                progressDialog.dismiss()
                                                                startActivity(intent)
                                                            } else {
                                                                intent.putExtra("BLOCKCHAIN_ADD_VERIFIED", 1)    //failed
                                                                println("Failed to add hash to blockchain.")
                                                                progressDialog.dismiss()
                                                                startActivity(intent)
                                                            }
                                                        }
                                                    } else {
                                                        if (result.toIntOrNull() == 0)
                                                            intent.putExtra("API_VERIFIED", 1)  //safe
                                                        else
                                                            intent.putExtra("API_VERIFIED", 2)  //suspicious

                                                        println("CNN-BiLSTM model invocated")

                                                        val benign = "benign"

                                                        deepLearningModelAPI(resolvedUrl) { predictedResult ->
                                                            if (predictedResult != null) {
                                                                if (predictedResult != benign) {
                                                                    intent.putExtra("IS_MALICIOUS", true)
                                                                    intent.putExtra("AI_VERIFIED", 1)    //Malicious
                                                                    Handler(Looper.getMainLooper()).post {
                                                                        Toast.makeText(this, "CNN-BiLSTM model detected url as $predictedResult -> Adding to blockchain", Toast.LENGTH_LONG).show()
                                                                    }
                                                                    println("----CNN-BiLSTM model detected url as $predictedResult--------")
                                                                    blockchainHelper.addHashToBlockchain(qrHash) { success ->
                                                                        if (success) {
                                                                            println("Hash added to blockchain successfully.")
                                                                            intent.putExtra("BLOCKCHAIN_ADD_VERIFIED", 0) //success
                                                                            progressDialog.dismiss()
                                                                            startActivity(intent)
                                                                        } else {
                                                                            intent.putExtra("BLOCKCHAIN_ADD_VERIFIED", 1) //failed
                                                                            println("Failed to add hash to blockchain.")
                                                                            progressDialog.dismiss()
                                                                            startActivity(intent)
                                                                        }
                                                                    }
                                                                } else {
                                                                    intent.putExtra("AI_VERIFIED", 0)    //Safe
                                                                    Handler(Looper.getMainLooper()).post {
                                                                        Toast.makeText(this, "The URL is safe (confirmed by DL model)", Toast.LENGTH_LONG).show()
                                                                    }
                                                                    println("The URL is safe (confirmed by DL model)")
                                                                    progressDialog.dismiss()
                                                                    startActivity(intent)
                                                                }
                                                            } else {
                                                                println("Error in predicted result")
                                                                progressDialog.dismiss()
                                                                Handler(Looper.getMainLooper()).post {
                                                                    Toast.makeText(this, "MODEL FETCH ERROR", Toast.LENGTH_LONG).show()
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                println("This QR code is benign.")
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {
                                when {
                                    Patterns.PHONE.matcher(qrContent)
                                        .matches() -> {
                                            val intent = Intent(Intent.ACTION_DIAL)
                                            intent.data = Uri.parse("tel:$qrContent")
                                            startActivity(intent)
                                    }

                                    qrContent.contains("@") && Patterns.EMAIL_ADDRESS.matcher(qrContent)
                                        .matches() -> {
                                            val intent = Intent(Intent.ACTION_SENDTO)
                                            intent.data = Uri.parse("mailto:$qrContent")
                                            startActivity(intent)
                                    }

                                    qrContent.startsWith("WIFI:") -> {
                                        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                                        startActivity(intent)
                                    }
                                    else -> {
                                        val intent = Intent(this, ResultActivity::class.java)
                                        intent.putExtra("IS_TEXT", scanResult.contents)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d("QRScan", "No scan result")
                }

            } else {
                println("QR scan was canceled.")
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startQRScanner()
            } else {
                println("camera permission required")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DebugTest", "Logging test in onCreate")
        setContent {
            SecureQrTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainContent(navController) }
                    composable("business_service") { BusinessServiceScreen(navController) }

                }
            }
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).not() &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startQRScanner() {
        selectedCompany = null
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0) // Use the default camera
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.setCaptureActivity(CaptureActivityPortrait::class.java)

        Handler(Looper.getMainLooper()).post { Log.d("TAG", "Your message") }

        Log.e("QRScan", "Launching QR scanner...")

        val scanIntent = integrator.createScanIntent()
        scanResultLauncher.launch(scanIntent)
    }

    @Composable
    fun MainContent(navController: NavController) {
        val activity = LocalContext.current as MainActivity

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F2027), Color(0xFF00000), Color(0xFF24243E))
                    )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.pic),
                contentDescription = "Cybersecurity Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "SecureQR : A Quantum Leap In Qr Code Security",
                    color = Color.Cyan,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f,2f),
                            blurRadius = 4f
                        )
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    if (ContextCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        activity.startQRScanner()
                    } else {
                        println("camera permission required")
                    }
                }) {
                    Text(text = "Scan QR Code")
                }

                Button(
                    onClick = { navController.navigate("business_service") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Business Service")
                }
            }
        }
    }

@Composable
fun BusinessServiceScreen(navController: NavController) {
    val activity = LocalContext.current as MainActivity
    var companies by remember { mutableStateOf(emptyList<Company>()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCompany by remember { mutableStateOf("Select Company") }
    var searchQuery by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        RetrofitClient.mongoApi.getCompanies().enqueue(object : Callback<List<Company>> {
            override fun onResponse(call: Call<List<Company>>, response: Response<List<Company>>) {
                if (response.isSuccessful) {
                    companies = response.body() ?: emptyList()
                } else {
                    println("API Error: Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<Company>>, t: Throwable) {
                println("API Error: ${t.message}")
            }
        })
    }

    val filteredCompanies = companies.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select a Company",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            fontFamily = FontFamily.Serif,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f,2f),
                    blurRadius = 4f
                )
            ))

        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expanded = true }) {
                Text(selectedCompany)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(250.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Company") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Column(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    filteredCompanies.forEach { company ->
                        DropdownMenuItem(
                            text = { Text(company.name) },
                            onClick = {
                                selectedCompany = company.name
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { activity.startBusinessQRScanner(selectedCompany) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Scan QR for $selectedCompany")
        }
    }
}




    private fun startBusinessQRScanner(company: String) {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code for $company")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.setCaptureActivity(CaptureActivityPortrait::class.java)

        selectedCompany = company
  
        val scanIntent = integrator.createScanIntent()
        scanResultLauncher.launch(scanIntent)
    }

    fun generateSHA256Hash(ipfsCID: String, hash: String): String {
        // Creating a JSONObject with EXACTLY the same structure as Node.js
        val jsonObject = JSONObject()
        jsonObject.put("ipfsCID", ipfsCID)
        jsonObject.put("qrhash", hash)

        val jsonString = jsonObject.toString()

        val bytes = jsonString.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)

        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun hashQrContent(content: String): String {
        val bytes = content.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(bytes)
        return hashedBytes.joinToString("") { "%02x".format(it) } // Convert bytes to hex
    }
}

fun getFinalRedirectedUrl(url: String, maxRedirects: Int = 5, callback: (String?) -> Unit) {
    val client = OkHttpClient.Builder()
        .followRedirects(false)
        .build()

    var requestUrl = url
    var redirects = 0

    Thread {
        try {
            while (redirects < maxRedirects) {
                val request = Request.Builder().url(requestUrl).build()
                val response = client.newCall(request).execute()

                if (response.isRedirect) {
                    requestUrl = response.header("Location") ?: break
                    redirects++
                } else {
                    callback(requestUrl) // Final URL
                    return@Thread
                }
            }
            if (redirects  >= maxRedirects)
                    println("Exceeded redirection limits: potential malicious url")
            callback(null) // Too many redirects or failed
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }.start()
}

private fun deepLearningModelAPI(urlToSend: String, callback: (String?) -> Unit) {

    val request = UrlRequest(urlToSend)

    RetrofitClient.instance.getPrediction(request).enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                val result = response.body()?.prediction
                callback(result)
            } else {
                println("failed api")
                callback(null)
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            println(t.message)
            callback(null)
        }
    })
}

private fun IPFSAPI(ipfsCID: String, callback: (IpfsResponse?) -> Unit) {
    RetrofitClient.ipfsApi.getProductData(ipfsCID).enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                try {
                    val jsonString = response.body()?.string()

                    val parsedJson = JsonParser().parse(jsonString).asJsonPrimitive.asString

                    val ipfsData = Gson().fromJson(parsedJson, IpfsResponse::class.java)

                    callback(ipfsData)
                } catch (e: Exception) {
                    println(e.message)
                    callback(null)
                }
            } else {
                println(response.errorBody()?.string())
                callback(null)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            println(t.message)
            callback(null)
        }
    })
}