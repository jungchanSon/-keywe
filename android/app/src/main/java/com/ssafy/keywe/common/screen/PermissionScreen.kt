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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.viewModel.PermissionViewModel
import com.ssafy.keywe.ui.theme.body1
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.orangeColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.webrtc.RemoteControlService

fun isAccessibilityServiceEnabled(
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

fun openAccessibilitySettingsAndGranted(context: Context): Boolean {
    return if (!isAccessibilityServiceEnabled(context, RemoteControlService::class.java)) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
        isAccessibilityServiceEnabled(context, RemoteControlService::class.java)
    } else {
        Log.d("MainActivity", "Accessibility Service already enabled")
        true
    }
}

@SuppressLint("InlinedApi")
@Composable
fun PermissionScreen(
    navController: NavHostController,
    viewModel: PermissionViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->
            val allGranted = grantedMap.values.all { it }
            navController.navigate(Route.AuthBaseRoute.SelectAppRoute)
        }



    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Spacer(modifier = Modifier.height(50.dp))
                    Column {
                        Text(
                            "키위 권한 요청 안내", style = h6sb
                        )
                        Spacer(modifier = Modifier.height(13.dp))
                        Text(
                            "키위는 아래 권한들을 필요로 합니다.", style = subtitle1
                        )
                        Text(
                            "서비스 사용 중 앱에서 요청 시 허용해주세요. ", style = subtitle1
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Column {
                        Text(
                            "필수 접근 권한", style = body1.copy(color = orangeColor)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PermissionRow("\uD83D\uDCF7   카메라", "프로필 이미지 등록을 위해 필요합니다.")
                        Spacer(modifier = Modifier.height(14.dp))
                        PermissionRow("\uD83D\uDCDE   전화", "키위 매칭을 위해 필요합니다.")
                        Spacer(modifier = Modifier.height(14.dp))
                        PermissionRow("\uD83C\uDFA4   음성", "실시간 사용을 위한 음성 지원이 필요합니다.")
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Column {
                        Text(
                            "선택 접근 권한", style = body1.copy(color = orangeColor)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PermissionRow("\uD83D\uDCC2   저장공간", "프로필 사진 저장을 위해 필요합니다.")
                        Spacer(modifier = Modifier.height(46.dp))
                        Text("✔ 선택 권한은 허용하지 않아도 앱을 이용할 수 있습니다.", style = subtitle2)
                    }
                }

                BottomButton(content = "확인", onClick = {
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
                    openAccessibilitySettingsAndGranted(context)

                    viewModel.saveIsFirstJoin(true)

                })
            }
        }

    }

}


@Composable
private fun PermissionRow(title: String, desc: String) {
    Row {
        Text(title, style = caption, modifier = Modifier.width(100.dp))
        Text(desc, style = caption)
    }
}