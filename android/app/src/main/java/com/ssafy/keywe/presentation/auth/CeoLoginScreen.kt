package com.ssafy.keywe.presentation.auth

import android.content.Context
import android.os.Build
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.SignUpRoute
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.presentation.auth.viewmodel.LoginViewModel
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.logo
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.primaryColor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CeoLoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current

    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val validForm by viewModel.validForm.collectAsStateWithLifecycle()
    val surface = rememberSaveable { mutableStateOf<Surface?>(null) }

    val onSearchExplicitlyTriggered = {
//        keyboardController?.hide()
        focusManager.clearFocus()
//        onSearchTriggered(searchQuery)
    }


    val context: Context = LocalContext.current


    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Route.MenuBaseRoute.KioskHomeRoute, builder = {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            })
        }
    }

    Box(modifier = modifier
//        .windowInsetsPadding(WindowInsets.systemBars)
        .noRippleClickable {
            focusManager.clearFocus()
        }
        .fillMaxSize()) {
        Column(
            modifier = modifier.padding(PaddingValues(horizontal = 24.dp))
        ) {
            Spacer(modifier = modifier.height(32.dp))
            Text(
                "KEYWE",
                style = logo,
                color = primaryColor,
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "사장님\n\n키위로 더 간단하고\n" + "스마트한 하루를 시작하세요.", style = h6sb
                )
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.ceo),
                    contentDescription = "Default Image",
                    modifier = modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(color = Color.Transparent),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = modifier.height(40.dp))
            DefaultTextFormField(
                label = "이메일",
                text = email,
                placeholder = "이메일을 입력해주세요.",
                isError = errorMessage != null,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                onValueChange = {
                    viewModel.onEmailChanged(it)
                },
            )
            Spacer(modifier = modifier.height(12.dp))
            DefaultTextFormField(
                label = "비밀번호",
                text = password,
                placeholder = "비밀번호를 입력해주세요.",
                isPassword = true,
                isError = errorMessage != null,
                onValueChange = {
                    viewModel.onPasswordChanged(it)
                },
            )
            if (errorMessage != null) Text(
                text = errorMessage!!,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = modifier.padding(top = 8.dp)
            )
            Spacer(modifier = modifier.height(32.dp))
            BottomButton(content = "로그인", onClick = {
                onSearchExplicitlyTriggered()
                viewModel.ceoLogin()
            }, enabled = validForm)
            Spacer(modifier = modifier.height(12.dp))
            Text(
                "계정이 없으신가요?",
                modifier = modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        navController.navigate(SignUpRoute)
                    },
                textAlign = TextAlign.Center,
                style = caption,
                textDecoration = TextDecoration.Underline
            )
        }
    }

}
