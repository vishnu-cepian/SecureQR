package com.example.secureqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import com.example.secureqr.ui.theme.SecureQrTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scannedContent = intent.getStringExtra("SCANNED_RESULT") ?: "No Data"

        setContent {
            SecureQrTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ResultScreen(scannedContent = scannedContent)
                }
            }
        }
    }
}

@Composable
fun ResultScreen(scannedContent: String) {
    val uriHandler = LocalUriHandler.current

    // Build AnnotatedString with LinkAnnotation for URLs
    val annotatedString = buildAnnotatedString {
        append("Scanned QR Content:\n\n")
        if (scannedContent.startsWith("http")) {
            // Annotate URL
            pushStringAnnotation(tag = "URL", annotation = scannedContent)
            pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
            append(scannedContent)
            pop() // Pop style
            pop() // Pop annotation
        } else {
            append(scannedContent)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Scanned QR Content:",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Render the AnnotatedString and handle clicks manually
        Text(
            text = annotatedString,
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(8.dp)
        )

        // Handle URL clicks manually
        if (scannedContent.startsWith("http")) {
            Modifier.clickable {
                uriHandler.openUri(scannedContent)
            }
        }
    }
}