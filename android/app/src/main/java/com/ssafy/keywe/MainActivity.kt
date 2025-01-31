package com.ssafy.keywe

//import androidx.hilt.navigation.compose.hiltViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.keywe.common.PermissionDialog
import com.ssafy.keywe.common.RationaleDialog
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultDialog
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.common.ext.dropShadow
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.BottomNavItem
import com.ssafy.keywe.presentation.auth.LoginScreen
import com.ssafy.keywe.presentation.auth.SignUpScreen
import com.ssafy.keywe.presentation.auth.viewmodel.LoginViewModel
import com.ssafy.keywe.presentation.order.MenuCartScreen
import com.ssafy.keywe.presentation.order.MenuDetailScreen
import com.ssafy.keywe.presentation.order.MenuScreen
import com.ssafy.keywe.presentation.profile.AddMemberScreen
import com.ssafy.keywe.presentation.profile.EditMember
import com.ssafy.keywe.presentation.profile.EmailVerification
import com.ssafy.keywe.presentation.profile.ProfileChoice
import com.ssafy.keywe.presentation.profile.ProfileScreen
import com.ssafy.keywe.presentation.splash.SplashScreen
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())
        retrieveFCMToken()
        val splashscreen = installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            // TokenManager의 이벤트를 전역적으로 구독
            LaunchedEffect(Unit) {
                tokenManager.tokenClearedEvent.collect {
                    // 로그인 화면으로 이동
                    navController.navigate("login") {
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


@Composable
fun MyApp(
    navController: NavHostController,
    tokenManager: TokenManager,
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    val state by navController.currentBackStackEntryAsState()
    // splash 와 login 은 topAppBar 없음
    val isShowTopAppBar: Boolean = state?.destination?.route?.let {
        it != "splash" && it != "login"
    } ?: false

    Scaffold(
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
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
//            composable("home") {
//                HomeScreen(navController, tokenManager)
//            }
            composable("login") { LoginScreen(navController) }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("profile") { ProfileScreen(navController) }
            composable("choiceProfile") { ProfileChoice(navController) }
            composable("editProfile") { EditMember(navController) }
            composable("emailVerify") { EmailVerification(navController) }
            composable("addProfile") { AddMemberScreen(navController) }
            composable("home") { MenuScreen(navController) }
            composable("menuDetail") { MenuDetailScreen(navController) }
            composable("menuCart") { MenuCartScreen(navController) }
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

@SuppressLint("RestrictedApi")
@Composable
private fun MyBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        BottomNavItem.Home, BottomNavItem.Profile, BottomNavItem.Login
    )


    AnimatedVisibility(
        visible = items.map { it.screenRoute }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = modifier
                .border(
                    border = BorderStroke(0.dp, Color.Black),
                    shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                )
                .dropShadow(
                    blurRadius = 18.dp
                ),
            containerColor = whiteBackgroundColor,
            contentColor = primaryColor,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.screenRoute,
                    colors = NavigationBarItemColors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        selectedIndicatorColor = whiteBackgroundColor,
                        unselectedIconColor = titleTextColor,
                        unselectedTextColor = titleTextColor,
                        disabledIconColor = titleTextColor,
                        disabledTextColor = titleTextColor,
                    ),
                    label = {
                        Text(
                            text = stringResource(id = item.title), style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    },
                    icon = {
                        Icon(
                            item.icon, contentDescription = stringResource(id = item.title)
                        )
                    },
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navBackStackEntry?.destination?.route?.let {
                                Timber.d("entry = $it")
                                popUpTo(it) {
                                    inclusive = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onClick: () -> Unit,
) {

    val textFieldValue: TextFieldValue = TextFieldValue()


    textFieldValue.selection
    var isShowDialog: Boolean by remember { mutableStateOf(false) }
    var isShowModal: Boolean by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()
    Column() {
        Text(text = "Hello $name!", modifier = modifier.clickable {
            isShowDialog = !isShowDialog
        })
        BottomButton(content = "show Modal", onClick = {
            isShowModal = true
        })
        if (isShowDialog) {
            DefaultDialog(
                title = "title", description = "description",
                onCancel = {
                    isShowDialog = false
                },
                onConfirm = {
                    isShowDialog = false
                },
            )
        }
        if (isShowModal) {

            DefaultModalBottomSheet(content = {
                Text("텍스트텍스트")
            }, sheetState = sheetState, onDismissRequest = {
                isShowModal = false
            }) {
                Row {
                    BottomButton(onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                isShowModal = false
                            }
                        }

                    }, content = "버튼")
                }
            }
        }

        DefaultTextFormField(
            label = "라벨",
            placeholder = "placeholder",
            text = text,
            onValueChange = { text = it })

        BottomButton(content = "호출", onClick = {
//            onClick()
            scope.launch {
                viewModel.logout()
            }


            Log.d("login", "request Login")
//            scope.launch {
//                viewModel.loginMITI()
//            }
        })
    }
}
