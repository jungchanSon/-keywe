package com.ssafy.keywe

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.ssafy.keywe.presentation.auth.LoginScreen
import com.ssafy.keywe.presentation.auth.SignUpScreen
import com.ssafy.keywe.presentation.splash.SplashScreen
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.HelperScreen
import com.ssafy.keywe.webrtc.ScreenSharing
import com.ssafy.keywe.webrtc.ScreenSizeManager
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
            val density = LocalDensity.current
            
            screenSizeManager.updateScreenSize(this, density)

            val navController = rememberNavController()
            NavControllerHolder.navController = navController
            // TokenManager의 이벤트를 전역적으로 구독
            LaunchedEffect(Unit) {
                tokenManager.tokenClearedEvent.collect {
                    // 로그인 화면으로 이동
                    navController.navigate(BottomRoute.LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            KeyWeTheme {
                MyApp(navController, tokenManager)
            }
        }

        // notification comes when app is killed
        val count: Int? = intent?.extras?.getString("count")?.toInt()
        Log.d("push notification", "on create count : $count")
        count?.let {
            PushNotificationManager.setDataReceived(count = count)
            lifecycleScope.launch {

                while (NavControllerHolder.navController == null) {
                    Log.d("push notification", "not logged in, waiting...")
                    delay(500)
                }

                NavControllerHolder.navController?.navigate("menuDetail")
//                while (rootNavigationViewModel.getMainNavigationViewModel() == null) {
//                    Log.d("push notification", "not logged in, waiting...")
//                    delay(500)
//                }
//                val mainNavigationController = rootNavigationViewModel.getMainNavigationViewModel()
//                mainNavigationController!!.showPushNotification()
                return@launch
            }
            return
        }

    }

    private fun handleNotification(intent: Intent) {
        val message = intent.getStringExtra("notification_message")
        message?.let {}
        Toast.makeText(this, "notification_message", Toast.LENGTH_SHORT).show()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotification(intent)
        NavControllerHolder.navController?.navigate("menuDetail")

        Log.d("push notification ", " on new intent extras? : ${intent?.extras}")

        // notification coming when app in inactive/background, data included in intent extra
        val count: Int? = intent?.extras?.getString("count")?.toInt()
        count?.let {
            Log.d("push notification ", " on new intent count : $count")
            PushNotificationManager.setDataReceived(count = count)
            lifecycleScope.launch {

//                while (rootNavigationViewModel.getMainNavigationViewModel() == null) {
//                    Log.d("push notification", "not logged in, waiting...")
//                    delay(100)
//                }
//
//                val mainNavigationController = rootNavigationViewModel.getMainNavigationViewModel()
//                mainNavigationController!!.showPushNotification()
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
                PushNotificationManager.registerTokenOnServer(token)
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
            }
            composable<BottomRoute.LoginRoute> {
                LoginScreen(
                    navController,
                )
            }
            composable<SignUpRoute> {
                SignUpScreen(navController)
            }
            profileGraph(navController)
            menuGraph(navController)
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
fun HomeScreen(navController: NavHostController, tokenManager: TokenManager) {
    val scope = rememberCoroutineScope()
    Column {
        DefaultAppBar(title = "title", navController = navController)
        TextButton(onClick = {
            scope.launch {
                tokenManager.clearTokens()
            }
        }) {
            Text(text = "토큰 초기화")
            Button(onClick = {
                navController.navigate(Route.MenuBaseRoute.MenuRoute)
            }) {
                Text("메뉴 라우팅")
            }
        }
    }
}


@Composable
fun ScreenCaptureView(onSurfaceReady: (Surface) -> Unit) {
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
        SurfaceView(context).apply {
            holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    onSurfaceReady(holder.surface) // Surface 준비되면 콜백 호출
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int,
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {}
            })
        }
    })
}

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
