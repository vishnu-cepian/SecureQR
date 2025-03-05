package com.example.secureqr

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
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
        val hashContent = intent.getStringExtra("HASHED_CONTENT") ?: "No Data"

        setContent {
            SecureQrTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ResultScreen(scannedContent = scannedContent, hashContent = hashContent)
                }
            }
        }
    }
}

//@Composable
//fun ResultScreen(scannedContent: String, hashContent: String) {
//    val uriHandler = LocalUriHandler.current
//
//
//    val annotatedString = buildAnnotatedString {
//        append("Scanned QR Content:\n\n")
//        if (scannedContent.startsWith("http") || scannedContent.startsWith("www" )) {
//            // Annotate URL
//            pushStringAnnotation(tag = "URL", annotation = scannedContent)
//            pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
//            append(scannedContent)
//            pop()
//            pop()
//        } else {
//            append(scannedContent)
//        }
//        append("\n\nSHA-256 HASHED CONTENT:\n\n")
//        append(hashContent)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Scanned QR Content:",
//            fontSize = 20.sp,
//            modifier = Modifier.padding(bottom = 10.dp)
//        )
//
//        Text(
//            text = annotatedString,
//            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
//            modifier = Modifier.padding(8.dp)
//                .clickable {
//                    if (scannedContent.startsWith("http" ) || scannedContent.startsWith("www" )) {
//                        uriHandler.openUri(scannedContent)
//                    }
//                }
//        )
//    }
@Composable
fun ResultScreen(scannedContent: String, hashContent: String) {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("Scanned QR Content:\n\n")
        if (scannedContent.startsWith("http") || scannedContent.startsWith("www")) {
            pushStringAnnotation(tag = "URL", annotation = scannedContent)
            pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
            append(scannedContent)
            pop()
            pop()
        } else {
            append(scannedContent)
        }
        append("\n\nSHA-256 HASHED CONTENT:\n\n")
        append(hashContent)
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

        Text(
            text = annotatedString,
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    if (scannedContent.startsWith("http") || scannedContent.startsWith("www")) {
                        val intent = Intent(context, SandboxedWebViewActivity::class.java).apply {
                            putExtra("URL", scannedContent)
                        }
                        context.startActivity(intent) // Open URL in sandboxed WebView
                    }
                }
        )
    }
}
