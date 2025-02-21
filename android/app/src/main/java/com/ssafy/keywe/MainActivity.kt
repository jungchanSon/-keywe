package com.ssafy.keywe

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.keywe.common.MyBottomNavigation
import com.ssafy.keywe.common.PermissionRoute
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.SignUpRoute
import com.ssafy.keywe.common.SplashRoute
import com.ssafy.keywe.common.authGraph
import com.ssafy.keywe.common.menuGraph
import com.ssafy.keywe.common.profileGraph
import com.ssafy.keywe.common.screen.PermissionScreen
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.domain.fcm.NotificationData
import com.ssafy.keywe.presentation.auth.SignUpScreen
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.presentation.splash.SplashScreen
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.ScreenSizeManager
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

object NavControllerHolder {
    var navController: NavController? = null
}

@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var screenSizeManager: ScreenSizeManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        retrieveFCMToken()
        val splashscreen = installSplashScreen()
        enableEdgeToEdge()
        setContent {

            val configuration = LocalConfiguration.current
            val density = LocalDensity.current

            val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
            Log.d("ScreenSize", "전체 화면 크기 (시스템 UI 포함) : $screenWidthPx x $screenHeightPx")


            val systemBarsInsets = WindowInsets.systemBars // 상태 바 + 네비게이션 바
            val imeInsets = WindowInsets.ime // 키보드 (IME) 높이

            val statusBarHeightPx = with(density) { systemBarsInsets.getTop(density) }
            val navBarHeightPx = with(density) { systemBarsInsets.getBottom(density) }
            val imeHeightPx = with(density) { imeInsets.getBottom(density) }

            val usableHeightPx = screenHeightPx - statusBarHeightPx - navBarHeightPx

            screenSizeManager.updateSystemSize(statusBarHeightPx)

            Log.d("ScreenSize", "navBarHeightPx: $statusBarHeightPx x $navBarHeightPx")
            Log.d("ScreenSize", "전체 화면 크기 (시스템 UI 제외) : $screenWidthPx x $usableHeightPx")


            // todo FCM device ID 추가
            val deviceId =
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)


            PushNotificationManager.updateDeviceId(deviceId)
            Log.d("push notification device id", deviceId)
            screenSizeManager.updateScreenSize(this, density)

            screenSizeManager.updateScreenMetrics(this, screenHeightPx, screenWidthPx)

            val navController = rememberNavController()
            NavControllerHolder.navController = navController
            // TokenManager의 이벤트를 전역적으로 구독
            LaunchedEffect(Unit) {
                tokenManager.tokenClearedEvent.collect {
                    // 로그인 화면으로 이동
                    navController.navigate(Route.AuthBaseRoute.SelectAppRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
            KeyWeTheme {
                val configuration = LocalConfiguration.current
                val density = LocalDensity.current

                val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx().toInt() }
                val screenHeightPx =
                    with(density) { configuration.screenHeightDp.dp.toPx().toInt() }
//                Log.d("ScreenSize", "전체 화면 크기 (시스템 UI 포함)222222 : $screenWidthPx x $screenHeightPx")
                MyApp(navController, tokenManager)
            }
        }

        // notification comes when app is killed
        val notification: NotificationData? =
            intent.getParcelableExtra<NotificationData>("notification")

        notification?.let {
            lifecycleScope.launch {
                while (NavControllerHolder.navController == null) {
                    Log.d("push notification", "not logged in, waiting...")
                    delay(500)
                }

                NavControllerHolder.navController!!.navigate(
                    Route.MenuBaseRoute.HelperWaitingRoomRoute(
                        it.storeId, it.kioskUserId, it.sessionId
                    )
                )

                return@launch
            }
            return
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // notification coming when app in inactive/background, data included in intent extra

        val notification = intent.getParcelableExtra<NotificationData>("notification")

        Log.d("FCM notification", "onNewIntent $notification")
        notification?.let {
            lifecycleScope.launch {
                while (NavControllerHolder.navController == null) {
                    Log.d("push notification", "not logged in, waiting...")
                    delay(500)
                }
                NavControllerHolder.navController!!.navigate(
                    Route.MenuBaseRoute.HelperWaitingRoomRoute(
                        it.storeId, it.kioskUserId, it.sessionId
                    )
                )
                return@launch
            }
            return
        }
    }


    private fun retrieveFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("push notification device token", "failed with error: ${task.exception}")
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("push notification device token", "token received: $token")
            lifecycleScope.launch {
                PushNotificationManager.updateToken(token)
            }
        })
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(
    navController: NavHostController,
    tokenManager: TokenManager,
) {

    val menuCartViewModel: MenuCartViewModel = hiltViewModel()
    val appBarViewModel: OrderAppBarViewModel = hiltViewModel()
    val keyWeViewModel: KeyWeViewModel = hiltViewModel()
    val signalViewModel: SignalViewModel = hiltViewModel()

    val state by navController.currentBackStackEntryAsState()
    // splash 와 login 은 topAppBar 없음
    val isShowTopAppBar: Boolean = state?.destination?.route?.let {
        it != "splash" && it != "login"
    } ?: false
    Scaffold(modifier = Modifier.background(whiteBackgroundColor),
        bottomBar = {
            MyBottomNavigation(
                navController = navController
            )
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SplashRoute> {
                SplashScreen(navController)
            }
            composable<SignUpRoute> {
                SignUpScreen(navController)
            }
            authGraph(navController, tokenManager)
            profileGraph(navController, tokenManager)
            menuGraph(
                navController, menuCartViewModel, appBarViewModel, signalViewModel, tokenManager,
            )


            composable<PermissionRoute> {
                PermissionScreen(navController)
            }
        }

    }


}
