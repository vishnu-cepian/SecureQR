package com.example.secureqr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {

    private val blockchainHelper = BlockchainHelper(this)
    private var selectedCompany: String? = null

    private val scanResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data

            val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, data)

            if (scanResult != null && scanResult.contents != null) {
                Log.d("QRScan", "Scan result: ${scanResult.contents}")
                var qrContent = scanResult.contents
                val qrHash = hashQrContent(qrContent)

                Log.d("QRScan", "Original Content: $qrContent")
                Log.d("QRScan", "SHA-256 Hash: $qrHash")
                val company = selectedCompany

                println("----- ${selectedCompany} -----------")
                if (company != null) {
                    println("------------INSIDE PRODUCT---------------")
                    blockchainHelper.isProductAuthentic(company, qrHash) { isAuthentic ->
                        if(isAuthentic) {
                            println("Product is authentic for $company")
                        }
                        else {
                            println("Product is NoT authentic for $company")
                        }
                    }
                } else {
                    blockchainHelper.checkIfHashExists(qrHash) { isMalicious ->
                        if (isMalicious) {
                            System.out.println("This QR code is Malicious.")
                        } else {
                            getFinalRedirectedUrl(qrContent) { finalUrl ->
                                val resolvedUrl = finalUrl ?: qrContent
                                println(resolvedUrl)
                                //  !!!! RUN API CHECK THEN ML CHECK THEN IF NOT SECURE SAVE TO BLOCKCHAIN !!

                            checkDomainReputation(resolvedUrl) {result ->
                                println(result)
                                val regex = """Reputation Score: (\d+)""".toRegex()
                                val matchResult = regex.find(result)

                                if (matchResult != null) {
                                    val score = matchResult.groupValues[1] // Extract the captured group
                                    println("Extracted Reputation Score: $score")

                                    val reputationScore = score.toIntOrNull() // Safely convert to an integer

                                    if (reputationScore != null) { // Check if conversion was successful
                                        if (reputationScore < 80) {
                                            println("The website is potentially malicious. Reputation Score: $reputationScore")
                                            handleMaliciousHash(qrHash)
                                        } else {
                                            println("The website is safe. Reputation Score: $reputationScore")
                                        }
                                    } else {
                                        println("Error: Could not extract a valid reputation score.")
                                    }
                                } else {
                                    println("Reputation score not found in the message.")
                                }
                            }
                            System.out.println("This QR code is benign.")
                            }
                        }
                    }

                    // Handle the QR code content (sending to ResultActivity)
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("SCANNED_RESULT", scanResult.contents) // adds data to intent
                    intent.putExtra("HASHED_CONTENT", qrHash)
                    startActivity(intent)   //start resultActivity
//                    Toast.makeText(this, "Scanned: ${scanResult.contents}", Toast.LENGTH_LONG)
//                        .show()
                }
            } else {
//                Toast.makeText(this, "No scan result", Toast.LENGTH_SHORT).show()
                Log.d("QRScan", "No scan result")
            }

        } else {
            showError("QR scan was canceled.")
        }
    }

    private fun handleMaliciousHash(qrHash: String) {
        blockchainHelper.addHashToBlockchain(qrHash) { success ->
            if (success) {
                System.out.println("Hash added to blockchain successfully.")
            } else {
                System.out.println("Failed to add hash to blockchain.")
            }
        }
    }


    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startQRScanner()
                } else {
                    System.out.println("camera persmission required")
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
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "SecureQR : A Quantum Leap In Qr Code Security")

            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    activity.startQRScanner()
                } else {
//                    Toast.makeText(activity, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                System.out.println("camera permission required")
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BusinessServiceScreen(navController: NavController) {
        val activity = LocalContext.current as MainActivity
        val companies = listOf("LuxuryBrand", "TechCorp", "FoodChain")

        var expanded by remember { mutableStateOf(false) }
        var selectedCompany by remember { mutableStateOf(companies[0]) } // Default selection

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select a Company", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                Button(onClick = { expanded = true }) {
                    Text(selectedCompany)  // Show selected company name
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    companies.forEach { company ->
                        DropdownMenuItem(
                            text = { Text(company) },
                            onClick = {
                                selectedCompany = company
                                expanded = false
                            }
                        )
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


    fun startBusinessQRScanner(company: String) {
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


    private fun hashQrContent(content: String): String {
        val bytes = content.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(bytes)
        return hashedBytes.joinToString("") { "%02x".format(it) } // Convert bytes to hex
    }
    private fun showError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun getFinalRedirectedUrl(url: String, maxRedirects: Int = 5, callback: (String?) -> Unit) {
    val client = OkHttpClient.Builder()
        .followRedirects(false) // Manually handle redirects
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
            callback(null) // Too many redirects or failed
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }.start()
}
