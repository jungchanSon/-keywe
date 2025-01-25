package com.ssafy.keywe

//import androidx.hilt.navigation.compose.hiltViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.auth0.android.jwt.JWT
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultDialog
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.LoginViewModel
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.util.JWTUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        lifecycleScope.launch {
            // Save tokens
            tokenManager.saveAccessToken("new_access_token")
            tokenManager.saveRefreshToken("new_refresh_token")

            // Retrieve tokens
            val accessToken = tokenManager.getAccessToken()?.first()
            val refreshToken = tokenManager.getRefreshToken()?.first()

            Log.d("TokenManager", "Access Token: $accessToken")
            Log.d("TokenManager", "Refresh Token: $refreshToken")

            val token =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims
            val claim = jwt.getClaim("isAdmin").asString() //get custom claims
            val isExpired = jwt.isExpired(10) // Do time validation with 10 seconds leeway

            Log.d(
                "jwt", "issuer = $issuer, claim = $claim, isExpired = $isExpired isTemp = ${
                    JWTUtil.isTempToken(token)
                }"
            )
        }

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
//                val dataStore = applicationContext.settingsTokenData
//                val tempToken = dataStore.data.map { // MyUserData의 data인 name
//                        settingsTokenData ->
//                    settingsTokenData.tempToken
//                }.collectAsState(initial = "")


                MyApp(navController)
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
fun MyApp(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Text("home")
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Text("home")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),//= hiltViewModel(),
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

            Log.d("login", "request Login")
            scope.launch {
                viewModel.loginMITI()
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyWeTheme {
        Greeting("Android", onClick = {})
    }
}


@Composable
fun ThousandFormatTextField() {
    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }
    TextField(value = text, onValueChange = { newInput ->
        val newValue = if (newInput.text.isNotBlank()) {
            newInput.text.clearThousandFormat().toLong().formatThousand()
        } else newInput.text

        text = newInput.copy(
            text = newValue, selection = TextRange(newValue.length)
        )
    }, placeholder = {
        Text(text = "Using textfieldvalue")
    }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

fun Long.formatThousand(): String {
    val decimalFormatter = DecimalFormat("#,###")
    return decimalFormatter.format(this)
}

fun String.clearThousandFormat(): String {
    return this.replace(",", "")
}