package com.ssafy.keywe.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.BottomRoute
import com.ssafy.keywe.common.PermissionRoute
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.SplashRoute
import com.ssafy.keywe.presentation.splash.viewmodel.SplashRouteType
import com.ssafy.keywe.presentation.splash.viewmodel.SplashViewModel
import com.ssafy.keywe.ui.theme.logo
import com.ssafy.keywe.ui.theme.primaryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val isDataLoaded = !remember { derivedStateOf { viewModel.isLoading.value } }.value
    val scope = rememberCoroutineScope()
    // 로고와 텍스트를 중심에 배치
    LaunchedEffect(isDataLoaded) {
        if (isDataLoaded) {
            scope.launch {
                delay(1500)
                when (viewModel.splashRouteType.value) {
                    SplashRouteType.SELECTAPP -> {
                        navController.navigate(Route.AuthBaseRoute.SelectAppRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }

                    SplashRouteType.PROFILE -> {
                        navController.navigate(Route.ProfileBaseRoute.ProfileChoiceRoute(true)) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }

                    SplashRouteType.HOME -> {
                        navController.navigate(BottomRoute.ProfileRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }

                    SplashRouteType.PERMISSION -> {
                        navController.navigate(PermissionRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }

                    SplashRouteType.KIOSK -> {
                        navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }
                }
            }
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로고 또는 아이콘
            Text(
                text = "KEYWE",
                style = logo,
                color = primaryColor,
                fontSize = 60.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

//            // 로딩 메시지
//            if (!isDataLoaded) {
//                Text(
//                    text = "데이터를 불러오는 중...", fontSize = 16.sp, color = Color.Gray
//                )
//            } else {
//                Text(
//                    text = "로딩 완료!", fontSize = 16.sp, color = Color.Green
//                )
//            }
        }
    }
}
