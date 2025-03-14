package com.example.secureqr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.secureqr.ui.theme.SecureQrTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scannedContent = intent.getStringExtra("SCANNED_RESULT") ?: "No Data"
        val isMalicious = intent.getBooleanExtra("IS_MALICIOUS", false)
        val isBlockchainVerified = intent.getBooleanExtra("BLOCKCHAIN_VERIFIED", false)
        val isApiCheck = intent.getIntExtra("API_VERIFIED", 3)
        val isAiCheck = intent.getIntExtra("AI_VERIFIED", 2)
        val isBlockchainAdd = intent.getIntExtra("BLOCKCHAIN_ADD_VERIFIED", 2)

        setContent {
            SecureQrTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultScreen(
                        scannedContent, isMalicious,
                        isBlockchainVerified, isApiCheck, isAiCheck, isBlockchainAdd
                    )
                }
            }
        }
    }
}

@Composable
fun ResultScreen(
    scannedContent: String,
    isMalicious: Boolean,
    isBlockchainVerified: Boolean,
    isApiCheck: Int,
    isAiCheck: Int,
    isBlockchainAdd: Int
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Main Heading
        Text(
            text = "Scan Results",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Safe or Malicious Status
        val statusText = if (isMalicious) "MALICIOUS" else "SAFE"
        val statusColor = if (isMalicious) listOf(Color.Red, Color(0xFFFF6B6B)) else listOf(Color.Green, Color(0xFF4CAF50))

        Text(
            text = statusText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Brush.linearGradient(colors = statusColor), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Parameter Checks Heading
        Text(
            text = "Parameters Checked",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Parameters List
        ParameterCheckBlockchain("Blockchain", isBlockchainVerified)
        ParameterCheckAPI("API", isApiCheck)
        ParameterCheckAI("AI MODEL", isAiCheck)
        ParameterCheckBlockchainReg("Blockchain Registration", isBlockchainAdd)

        Spacer(modifier = Modifier.height(20.dp))

        // QR Content
        Text(
            text = "QR Code Data:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        val annotatedString = buildAnnotatedString {
            if (scannedContent.startsWith("http") || scannedContent.startsWith("www")) {
                pushStringAnnotation(tag = "URL", annotation = scannedContent)
                pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
                append(scannedContent)
                pop()
                pop()
            } else {
                append(scannedContent)
            }
        }

        Text(
            text = annotatedString,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    if (scannedContent.startsWith("http") || scannedContent.startsWith("www")) {
                        val intent = Intent(context, SandboxedWebViewActivity::class.java).apply {
                            putExtra("URL", scannedContent)
                        }
                        context.startActivity(intent)
                    }
                }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        if (isMalicious) {
            Button(
                onClick = {
                    val intent = Intent(context, SandboxedWebViewActivity::class.java).apply {
                        putExtra("URL", scannedContent)
                    }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Open in Secure Mode", color = Color.White)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = android.net.Uri.parse(scannedContent)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Open in Default Browser")
                }

                Button(
                    onClick = {
                        val intent = Intent(context, SandboxedWebViewActivity::class.java).apply {
                            putExtra("URL", scannedContent)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Open in Secure Web")
                }
            }
        }
    }
}

// Reusable Composable for Parameter Check
@Composable
fun ParameterCheckBlockchain(parameterName: String, isVerified: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = parameterName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = if (isVerified) "✔ Verified" else "✖ Found Malicious",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isVerified) Color.Green else Color.Red
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ParameterCheckAPI(parameterName: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = parameterName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(

            text = if (value == 1) "✔ Safe" else if(value == 2) "⚠ Suspicious" else if(value == 0) "✖ Malicious" else "Terminated",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == 1) Color.Green else if(value == 2) Color.Yellow else if(value == 0) Color.Red else Color.Cyan
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ParameterCheckAI(parameterName: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = parameterName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(

            text = if (value == 0) "✔ Safe" else if(value == 1) "✖ Malicious" else "Terminated",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == 0) Color.Green else if(value == 1) Color.Red else Color.Cyan
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ParameterCheckBlockchainReg(parameterName: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = parameterName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(

            text = if (value == 0) "✔ Success" else if(value == 1) "✖ Failed" else "Terminated",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == 0) Color.Green else if(value == 1) Color.Red else Color.Cyan
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}