package com.accountmanager.smartledger.presentation.screens

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

@Composable
fun PdfViewerScreen() {
    val pdfUrl =
        "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"

    Column(modifier = Modifier.fillMaxSize()) {
        PdfViewer(url = pdfUrl)
    }
}


@Composable
fun PdfViewer(url: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    Log.e("PdfViewer", "Error loading PDF: $description (Error code: $errorCode)")
                }
            }
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            // Enable safe browsing (optional)
            if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_ENABLE)) {
                WebSettingsCompat.setSafeBrowsingEnabled(settings, true)
            }

            // Load the PDF URL
            loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        }
    }, modifier = Modifier.fillMaxSize())
}
