package com.big9.app.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.big9.app.R
import com.big9.app.databinding.ActivityWebviewBinding


class WebviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)

        binding.webview.webViewClient = MyWebViewClient()
        val url = "https://www.gibl.in/wallet/validate2/"
        val postData = "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        }
        binding.webview.postUrl(url, postData.toByteArray())
        binding.btnClick.setOnClickListener{

        }
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return false
        }
    }
}