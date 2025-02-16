package com.ssafy.keywe.presentation.kiosk

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.screen.openAccessibilitySettingsAndGranted
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.kiosk.component.SelectOptionCard
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@Composable
fun KioskHomeScreen(
    navController: NavController,
    tokenManager: TokenManager,
) {
    val storeId = tokenManager.cachedStoreId

//    val systemUiController = rememberSystemUiController()

    // 시스템 UI 숨기기
//    SideEffect {
//        systemUiController.isSystemBarsVisible = false
//    }


    val context = LocalContext.current
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->
            val allGranted = grantedMap.values.all { it }
            if (allGranted) {
                // Permission is granted
                if (openAccessibilitySettingsAndGranted(context)) {
                    navController.navigate(Route.MenuBaseRoute.KioskPhoneNumberRoute)
                } else {
                    Toast.makeText(context, "매칭에 필요한 권한이 허용되지 않았습니다.", Toast.LENGTH_LONG).show()
                }
            } else {
                // Permission is denied
                Toast.makeText(context, "매칭에 필요한 권한이 허용되지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBackgroundColor)
            .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "KEYWE",
            color = primaryColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "어디서 드시겠습니까?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "주문 후에는 컵 변경이 불가한 점 양해 부탁드립니다.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        SelectOptionCard(title = "테이크아웃(포장)",
            description = "1회용 컵\n자원 재활용법에 따라 매장 내 일회용 컵 사용이 금지되어 있습니다.",
            imageRes = R.drawable.americano,
            backgroundColor = Color(0xFFF8B991),
            onClick = { navController.navigate(Route.MenuBaseRoute.MenuRoute(storeId ?: 0)) })

        SelectOptionCard(title = "매장에서 먹을게요",
            description = "다회용 컵 제공",
            imageRes = R.drawable.espresso,
            backgroundColor = Color(0xFFF9CB75),
            onClick = { navController.navigate(Route.MenuBaseRoute.MenuRoute(storeId ?: 0)) })

        SelectOptionCard(title = "키위 매칭",
            description = "키오스크 대리주문\n자신 또는 제 3자에게 대리 주문이 가능합니다.",
            imageRes = R.drawable.kiwibird,
            backgroundColor = primaryColor,
            onClick = {
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
            })
    }
}