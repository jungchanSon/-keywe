package com.ssafy.keywe.common.screen

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.webrtc.RemoteControlService

private fun isAccessibilityServiceEnabled(
    context: Context,
    service: Class<out AccessibilityService>,
): Boolean {
    val expectedComponentName = ComponentName(context, service)
    val enabledServices = Settings.Secure.getString(
        context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    val accessibilityEnabled =
        Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)

    if (accessibilityEnabled == 1) {
        enabledServices?.split(":")?.forEach { componentName ->
            if (ComponentName.unflattenFromString(componentName) == expectedComponentName) {
                return true
            }
        }
    }
    return false
}

fun openAccessibilitySettings(context: Context) {
    if (!isAccessibilityServiceEnabled(context, RemoteControlService::class.java)) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    } else {
        Log.d("MainActivity", "Accessibility Service already enabled")
    }
}

@SuppressLint("InlinedApi")
@Composable
fun PermissionScreen(navController: NavHostController) {
    val context = LocalContext.current

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->
            val allGranted = grantedMap.values.all { it }
            if (allGranted) {
                // Permission is granted
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show()
                navController.navigate(Route.AuthBaseRoute.SelectAppRoute)
            } else {
                // Permission is denied
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }



    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding), verticalArrangement = Arrangement.Center
        ) {
            Text("권한 요청")
            BottomButton(content = "권한 요청", onClick = {
                permissionLauncher.launch(
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO,
                        )
                    } else {
                        arrayOf(
                            Manifest.permission.POST_NOTIFICATIONS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                        )
                    }
                )
                openAccessibilitySettings(context)
            })
        }
    }

}