package com.glam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.os.Looper
import android.os.Handler



class MainActivity : ReactActivity() {

    private var webPermissionRequest: PermissionRequest? = null

    override fun getMainComponentName(): String = "glam"

    override fun onCreate(savedInstanceState: Bundle?) {
     
        super.onCreate(null)

        // Enable WebView settings
        enableWebViewSettings()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

    // Enable WebView settings for camera permissions
    private fun enableWebViewSettings() {
                WebView.setWebContentsDebuggingEnabled(true)
                ////add looper wait time to 1000 or more seconds if it still does not work. 
        Looper.myLooper()?.let {
            Handler(it).post {
        WebView(this).webChromeClient = CustomWebChromeClient()
            }
        }
    }

    // WebView Camera Permission Handler
    inner class CustomWebChromeClient : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest) {
            runOnUiThread {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        request.grant(request.resources)
                    } else {
                        webPermissionRequest = request
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                } else {
                    request.grant(request.resources)
                }
            }
        }
    }

    // Handle camera permission result
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            webPermissionRequest?.grant(webPermissionRequest?.resources)
        }
    }
}