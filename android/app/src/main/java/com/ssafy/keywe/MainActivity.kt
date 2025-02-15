package com.ssafy.keywe

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.keywe.common.BottomRoute
import com.ssafy.keywe.common.HelperRoute
import com.ssafy.keywe.common.LoginRoute
import com.ssafy.keywe.common.MyBottomNavigation
import com.ssafy.keywe.common.PermissionDialog
import com.ssafy.keywe.common.RationaleDialog
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.SharingRoute
import com.ssafy.keywe.common.SignUpRoute
import com.ssafy.keywe.common.SplashRoute
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.menuGraph
import com.ssafy.keywe.common.profileGraph
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.websocket.SignalService
import com.ssafy.keywe.data.websocket.SignalType
import com.ssafy.keywe.domain.fcm.NotificationData
import com.ssafy.keywe.presentation.auth.LoginScreen
import com.ssafy.keywe.presentation.auth.SignUpScreen
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.presentation.splash.SplashScreen
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.HelperScreen
import com.ssafy.keywe.webrtc.ScreenSharing
import com.ssafy.keywe.webrtc.ScreenSizeManager
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


//        val windowMetrics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            windowManager.currentWindowMetrics
//        } else {
//            TODO("VERSION.SDK_INT < R")
//        }
//        val fullWidthPx = windowMetrics.bounds.width()
//        val fullHeightPx = windowMetrics.bounds.height()
//
//        Log.d("ScreenSize", "전체 화면 크기 (시스템 UI 포함) : $fullWidthPx x $fullHeightPx")


        Timber.plant(Timber.DebugTree())
        retrieveFCMToken()
        val splashscreen = installSplashScreen()
        enableEdgeToEdge()
        setContent {

            val configuration = LocalConfiguration.current
            val density = LocalDensity.current

            val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx().toInt() }
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx().toInt() }
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

            val navController = rememberNavController()
            NavControllerHolder.navController = navController
            // TokenManager의 이벤트를 전역적으로 구독
            LaunchedEffect(Unit) {
                tokenManager.tokenClearedEvent.collect {
                    // 로그인 화면으로 이동
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            KeyWeTheme {
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
//    fcmViewModel: FCMViewModel = hiltViewModel(),
) {


//    val client = StreamVideoBuilder(
//        context = context,
//        apiKey = "pqyd87b8sraa",
//        geo = GEO.GlobalEdgeNetwork,
//        user = user,
//        token = token,
//    ).build()
//
//    val call = client.call("default", "123")
//    val joinResult = call.join(create = true)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    val menuCartViewModel: MenuCartViewModel = hiltViewModel()
    val appBarViewModel: OrderAppBarViewModel = hiltViewModel()

    val state by navController.currentBackStackEntryAsState()
    // splash 와 login 은 topAppBar 없음
    val isShowTopAppBar: Boolean = state?.destination?.route?.let {
        it != "splash" && it != "login"
    } ?: false
    Scaffold(modifier = Modifier.background(whiteBackgroundColor),
//        topBar = {
//            if (isShowTopAppBar) DefaultAppBar("title", navController = navController)
//        },
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
            composable<BottomRoute.HomeRoute> {
                HomeScreen(navController, tokenManager)
//                InputPhoneNumberScreen(navController)
            }
            composable<LoginRoute> { LoginScreen(navController) }
            composable<SignUpRoute> {
                SignUpScreen(navController)
            }
            profileGraph(navController, tokenManager)
            menuGraph(navController, menuCartViewModel, appBarViewModel)
//            kioskGraph(navController)
            composable<SharingRoute> {
                ScreenSharing()
            }
            composable<HelperRoute> {
                val arg = it.toRoute<HelperRoute>()
                val channelName = arg.channelName

//                val context = LocalContext.current
//                val viewModel: KeyWeViewModel = hiltViewModel()
                HelperScreen(channelName, navController, {
                    Log.d("Helper Back", "interceptor Back")
                })
            }
//            composable<Route.MenuBaseRoute.WaitingRoomRoute> {
//                val arg = it.toRoute<Route.MenuBaseRoute.WaitingRoomRoute>()
//                WaitingRoomScreen(
//                    navController = navController,
//                    sessionId = arg.sessionId,
//                    storeId = arg.storeId,
//                    kioskUserId = arg.kioskUserId
//                )
//            }
//            kioskGraph(navController)
        }

    }


}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {


    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)


    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}


@Composable
fun HomeScreen(
    navController: NavHostController, tokenManager: TokenManager,
    viewModel: SignalViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
//    val message by viewModel.messageFlow.collectAsState(initial = null)
//
//    if (message != null) {
//        Text(text = "메시지: ${message.toString()}")
//    } else {
//        Text(text = "메시지 대기중...")
//    }


    val profileId = 677367955509677381
    val sessionId = 677367955509677381
    val storeId = 677367955509677381
    val kioskUserId = 677367955509677381

    Column {
        DefaultAppBar(title = "title", navController = navController)
        TextButton(onClick = {
            scope.launch {
                tokenManager.clearTokens()
            }
        }) {
            Text(text = "토큰 초기화")
        }
        Button(onClick = {
            navController.navigate(Route.MenuBaseRoute.MenuRoute)
//                navController.navigate(Route.KioskBaseRoute.KioskPhoneNumberRoute)
        }) {
            Text("메뉴 라우팅")
        }
        TextButton(onClick = {
//            val intent = Intent(context, SignalService::class.java)
//            intent.action = SignalType.CONNECT.name
//            context.startService(intent)
            navController.navigate(
                Route.MenuBaseRoute.HelperWaitingRoomRoute(
                    sessionId = sessionId.toString(),
                    storeId = storeId.toString(),
                    kioskUserId = kioskUserId.toString()
                )
            )
        }) { Text("키위 요청") }
        TextButton(onClick = {
            val intent = Intent(context, SignalService::class.java)
            intent.putExtra(
                "profileId", profileId.toString()
            )
            context.startService(intent)
        }) { Text("구독 연결") }

        TextButton(onClick = {
            val intent = Intent(context, SignalService::class.java)
            intent.action = SignalType.REQUEST.name
            intent.putExtra(
                "storeId", storeId.toString()
            )
            context.startService(intent)
        }) { Text("원격 연결") }
        TextButton(onClick = {
            val intent = Intent(context, SignalService::class.java)
            intent.action = SignalType.ACCEPT.name
            intent.putExtra(
                "sessionId", sessionId.toString()
            )
            context.startService(intent)
        }) { Text("주문 수락") }
        TextButton(onClick = {
            val intent = Intent(context, SignalService::class.java)
            intent.action = SignalType.CLOSE.name
            intent.putExtra(
                "sessionId", sessionId.toString()
            )
            context.startService(intent)
        }) { Text("주문 종료") }
        Button(onClick = {
//            navController.navigate(Route.MenuBaseRoute.MenuRoute)
            navController.navigate(Route.MenuBaseRoute.KioskPhoneNumberRoute)
        }) {
            Text("키오스크")
        }
        Button(onClick = {
//            navController.navigate(Route.MenuBaseRoute.MenuRoute)
//            navController.navigate(Route.MenuBaseRoute.KioskPhoneNumberRoute)
            navController.navigate((Route.MenuBaseRoute.KioskHomeRoute))
        }) {
            Text("키오스크 시작 단계")
        }
    }
}


//@Composable
//fun ScreenCaptureView(onSurfaceReady: (Surface) -> Unit) {
//    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
//        SurfaceView(context).apply {
//            holder.addCallback(object : SurfaceHolder.Callback {
//                override fun surfaceCreated(holder: SurfaceHolder) {
//                    onSurfaceReady(holder.surface) // Surface 준비되면 콜백 호출
//                }
//
//                override fun surfaceChanged(
//                    holder: SurfaceHolder,
//                    format: Int,
//                    width: Int,
//                    height: Int,
//                ) {
//                }
//
//                override fun surfaceDestroyed(holder: SurfaceHolder) {}
//            })
//        }
//    })
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Greeting(
//    name: String,
//    modifier: Modifier = Modifier,
//    viewModel: LoginViewModel = hiltViewModel(),
//    onClick: () -> Unit,
//) {
//
//    val textFieldValue: TextFieldValue = TextFieldValue()
//
//
//    textFieldValue.selection
//    var isShowDialog: Boolean by remember { mutableStateOf(false) }
//    var isShowModal: Boolean by remember { mutableStateOf(false) }
//    var text by remember { mutableStateOf("") }
//
//    val sheetState = rememberModalBottomSheetState()
//
//    val scope = rememberCoroutineScope()
//    Column() {
//        Text(text = "Hello $name!", modifier = modifier.clickable {
//            isShowDialog = !isShowDialog
//        })
//        BottomButton(content = "show Modal", onClick = {
//            isShowModal = true
//        })
//        if (isShowDialog) {
//            DefaultDialog(
//                title = "title", description = "description",
//                onCancel = {
//                    isShowDialog = false
//                },
//                onConfirm = {
//                    isShowDialog = false
//                },
//            )
//        }
//        if (isShowModal) {
//
//            DefaultModalBottomSheet(content = {
//                Text("텍스트텍스트")
//            }, sheetState = sheetState, onDismissRequest = {
//                isShowModal = false
//            }) {
//                Row {
//                    BottomButton(onClick = {
//                        scope.launch {
//                            sheetState.hide()
//                        }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                isShowModal = false
//                            }
//                        }
//
//                    }, content = "버튼")
//                }
//            }
//        }
//
//        DefaultTextFormField(
//            label = "라벨",
//            placeholder = "placeholder",
//            text = text,
//            onValueChange = { text = it })
//
//        BottomButton(content = "호출", onClick = {
////            onClick()
//            scope.launch {
//                viewModel.logout()
//            }
//
//
//            Log.d("login", "request Login")
////            scope.launch {
////                viewModel.loginMITI()
////            }
//        })
//    }
//}
