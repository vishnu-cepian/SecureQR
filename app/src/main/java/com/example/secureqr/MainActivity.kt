package com.example.secureqr

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.secureqr.ui.theme.SecureQrTheme
import com.google.zxing.integration.android.IntentIntegrator
import android.util.Log
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.security.MessageDigest

class MainActivity : ComponentActivity() {

    // Registering for the result of QR scan
    private val scanResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
            Toast.makeText(this, "QR Content Hashed", Toast.LENGTH_LONG).show()
            Log.d("QRScan", "Original Content: $qrContent")
            Log.d("QRScan", "SHA-256 Hash: $qrHash")
            if (scanResult.contents == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // Handle the QR code content (e.g., sending to backend for verification)
                val intent = Intent(this,ResultActivity::class.java)
                intent.putExtra("SCANNED_RESULT", scanResult.contents)
                startActivity(intent)
                Toast.makeText(this, "Scanned: ${scanResult.contents}", Toast.LENGTH_LONG).show()
                // You can send scanResult.contents to your API for further verification
            }
        } else {
            Toast.makeText(this, "No scan result", Toast.LENGTH_SHORT).show()
            Log.d("QRScan", "No scan result")
        }
    }

    // Use ActivityResultContracts.RequestPermission to handle permission request
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, you can now start scanning
                startQRScanner()
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Request camera permission if not granted
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).not() &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            // If permission is already granted, start QR scanner
            startQRScanner()
        }
    }

    // Start QR scanning when button is clicked
    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0) // Use the default camera
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)

        // Log to ensure QR scanner is launched
        Log.d("QRScan", "Launching QR scanner...")

        val scanIntent = integrator.createScanIntent()
        scanResultLauncher.launch(scanIntent)
    }

    @Composable
    fun MainContent(innerPadding: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "SecureQR : A Quantum Leap In Qr Code Security")

            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { startQRScanner() }) {
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

    fun hashQrContent(content: String): String {
        val bytes = content.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(bytes)
        return hashedBytes.joinToString("") { "%02x".format(it) } // Convert bytes to hex
    }
}

