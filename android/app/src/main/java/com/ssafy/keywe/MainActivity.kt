package com.ssafy.keywe

//import androidx.hilt.navigation.compose.hiltViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultDialog
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.BottomNavItem
import com.ssafy.keywe.presentation.auth.LoginScreen
import com.ssafy.keywe.presentation.auth.SignUpScreen
import com.ssafy.keywe.presentation.auth.viewmodel.LoginViewModel
import com.ssafy.keywe.presentation.splash.SplashScreen
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        val splashscreen = installSplashScreen()
        enableEdgeToEdge()


//        splashscreen.setKeepOnScreenCondition { viewModel.isLoading.value }


//        lifecycleScope.launch {
//            // Save tokens
//            tokenManager.saveAccessToken("new_access_token")
//            tokenManager.saveRefreshToken("new_refresh_token")
//
//            // Retrieve tokens
//            val accessToken = tokenManager.getAccessToken()?.first()
//            val refreshToken = tokenManager.getRefreshToken()?.first()
//
//            Log.d("TokenManager", "Access Token: $accessToken")
//            Log.d("TokenManager", "Refresh Token: $refreshToken")
//
//            val token =
//                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
//            val jwt = JWT(token)
//
//            val issuer = jwt.issuer //get registered claims
//            val claim = jwt.getClaim("isAdmin").asString() //get custom claims
//            val isExpired = jwt.isExpired(10) // Do time validation with 10 seconds leeway
//
//            Log.d(
//                "jwt", "issuer = $issuer, claim = $claim, isExpired = $isExpired isTemp = ${
//                    JWTUtil.isTempToken(token)
//                }"
//            )
//        }

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
//                Scaffold(
//                    topBar = { DefaultAppBar(title = "타이틀") }, modifier = Modifier.fillMaxSize()
//                ) { innerPadding ->
//                    Greeting(name = "Android",
//                        modifier = Modifier.padding(innerPadding),
//                        onClick = {
//                            lifecycleScope.launch {
//
//
////                                dataStore.updateData { currentNames ->
////                                    currentNames.toBuilder()
////                                        .setTempToken("tempToken")
////                                        .build()
////                                }
//                            }
//                        })
//                }
            }
        }

    }
}


@Composable
fun MyApp(
    navController: NavHostController,
    tokenManager: TokenManager,
) {
    val state by navController.currentBackStackEntryAsState()
    // splash 와 login 은 topAppBar 없음
    val isShowTopAppBar: Boolean = state?.destination?.route?.let {
        it != "splash" && it != "login"
    } ?: false

//    val isShowTopAppBar: Boolean = navController.currentBackStackEntry?.destination?.route?.let {
//        it != "splash" && it != "login"
//    } ?: false

    Scaffold(topBar = {
        if (isShowTopAppBar) DefaultAppBar("title", navController = navController)
    }, bottomBar = {
        MyBottomNavigation(
            containerColor = Color.Green,
            contentColor = Color.White,
            indicatorColor = Color.Green,
            navController = navController
        )
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController, startDestination = "splash"
            ) {
                composable("splash") {
                    SplashScreen(navController)
                }
                composable("home") { HomeScreen(navController, tokenManager) }
                composable("login") { LoginScreen(navController) }
                composable("signup") {
                    SignUpScreen(navController)
                }
                composable("profile") { ProfileScreen() }
            }
        }

    }


}


@SuppressLint("RestrictedApi")
@Composable
private fun MyBottomNavigation(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    indicatorColor: Color,
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
            modifier = modifier,
            containerColor = whiteBackgroundColor,
            contentColor = contentColor,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.screenRoute,
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
fun ProfileScreen() {
    Text(text = "Profile")
}

@Composable
fun HomeScreen(navController: NavHostController, tokenManager: TokenManager) {

    val scope = rememberCoroutineScope()
    Text("home")
    TextButton(onClick = {
        scope.launch {
            tokenManager.clearTokens()
        }
    }) {
        Text(text = "토큰 초기화")
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

        DefaultTextFormField(label = "라벨",
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
