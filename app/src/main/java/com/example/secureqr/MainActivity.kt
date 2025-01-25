package com.example.secureqr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.secureqr.ui.theme.SecureQrTheme
import com.google.zxing.integration.android.IntentIntegrator
import java.security.MessageDigest
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {

    private val blockchainHelper = BlockchainHelper(this)

    private val scanResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val rc = result.resultCode
        val ac = Activity.RESULT_OK
        if (rc == ac) {
            val data = result.data
//        Log.d("QRScan", "Barcode Format: ${result.data?.toString()}")
            val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, data)
//        Log.d("QRScan", "Result Code: ${result.resultCode}")
//        Log.d("QRScan", "Scan:$scanResult")
//        Log.d("QRScan", "Scan result contents: ${scanResult.contents}")
//       Log.d("QRScan", "Scan result format: ${scanResult.formatName}")
            if (scanResult != null) {
                Log.d("QRScan", "Scan result: ${scanResult.contents}")
                val qrContent = scanResult.contents
                val qrHash = hashQrContent(qrContent)
//                Toast.makeText(this, "QR Content Hashed", Toast.LENGTH_LONG).show()
                Log.d("QRScan", "Original Content: $qrContent")
                Log.d("QRScan", "SHA-256 Hash: $qrHash")
                if (scanResult.contents == null) {
                    System.out.println("scan cancelled")
//                    Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
                } else {

                    blockchainHelper.checkIfHashExists(qrHash) { isMalicious ->
                        if (isMalicious) {
                            System.out.println("This QR code is malicious.")
//                            Toast.makeText(this, "Malicious", Toast.LENGTH_SHORT).show()
                            // Handle malicious QR code (e.g., alert user, log data, etc.)
                        } else {
                            System.out.println("This QR code is benign.")
                            blockchainHelper.addHashToBlockchain(qrHash) { success ->
                                if (success) {
                                    System.out.println("Hash added to blockchain successfully.")
                                } else {
                                    System.out.println("Failed to add hash to blockchain.")
                                }
                            }
//                            Toast.makeText(this, "Benign", Toast.LENGTH_SHORT).show()
                            // You can pass the benign hash to the machine learning part or store it for future checks
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


        private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startQRScanner()
                } else {
//                    Toast.makeText(
//                        this,
//                        "Camera permission is required to scan QR codes.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    System.out.println("camera persmission required")
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DebugTest", "Logging test in onCreate")
        setContent {
            SecureQrTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainContent(innerPadding = innerPadding)
                    }
                )
            }
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).not() &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startQRScanner() {
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
    fun MainContent(innerPadding: PaddingValues) {
        val activity = LocalContext.current as MainActivity
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SecureQrTheme {
            MainContent(innerPadding = PaddingValues())
        }
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

