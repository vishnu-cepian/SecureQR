package com.example.secureqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.secureqr.ui.theme.SecureQrTheme

class ProductResultActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAuthentic = intent.getBooleanExtra("AUTHENTIC", false)
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "No Data"
        val serialNumber = intent.getStringExtra("SERIAL_NUMBER") ?: "No Data"
        val batch = intent.getStringExtra("BATCH") ?: "No Data"
        val companyName = intent.getStringExtra("COMPANY_NAME") ?: "No Data"

        setContent {
            SecureQrTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultScreen(isAuthentic, productName, serialNumber, batch, companyName)
                }
            }
        }
    }
}

@Composable
fun ResultScreen(isAuthentic: Boolean, productName: String, serialNumber: String, batch: String, companyName: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Scan Results",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val statusText = if (!isAuthentic) "COUNTERFEIT PRODUCT" else "AUTHENTIC PRODUCT"
        val statusColor = if (!isAuthentic) listOf(Color.Red, Color(0xFFFF6B6B)) else listOf(
            Color.Green,
            Color(0xFF4CAF50)
        )

        Text(
            text = statusText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(
                    Brush.linearGradient(colors = statusColor),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 20.dp, vertical = 8.dp),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(50.dp))
        if (isAuthentic) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProductDetailRow(label = "Company Name", value = companyName)
                ProductDetailRow(label = "Product Name", value = productName)
                ProductDetailRow(label = "Serial Number", value = serialNumber)
                ProductDetailRow(label = "Batch", value = batch)
            }
        } else {
            Text(
                text = "WARNING: This product is NOT authentic!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ProductDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Cyan)
    }
}