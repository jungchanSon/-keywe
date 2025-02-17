package com.ssafy.keywe.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.presentation.auth.viewmodel.SignUpViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current

    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val passwordCheck by viewModel.passwordCheck.collectAsStateWithLifecycle()

    val valid by viewModel.validForm.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isSigned by viewModel.isSignIn.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(isSigned) {
        if (isSigned) {
            navController.navigate(Route.AuthBaseRoute.LoginRoute) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }

        }
    }
    Scaffold(topBar = {
        DefaultAppBar(title = "회원가입", navController = navController)
    }) {
        Box(modifier = modifier
            .fillMaxSize()
            .clickable {
                focusManager.clearFocus()
            }) {
            Column(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Spacer(modifier.height(32.dp))
                    DefaultTextFormField(
                        label = "이메일",
                        text = email,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        placeholder = "이메일을 입력해주세요.",
                        onValueChange = {
                            viewModel.onEmailChanged(it)
                        },
                    )
                    Spacer(modifier.height(12.dp))
                    DefaultTextFormField(
                        label = "비밀번호",
                        text = password,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        placeholder = "비밀번호를 입력해주세요.",
                        isError = errorMessage != null,
                        isPassword = true,
                        onValueChange = {
                            viewModel.onPasswordChanged(it)
                        },
                    )
                    Spacer(modifier.height(12.dp))
                    DefaultTextFormField(
                        label = "비밀번호 확인",
                        text = passwordCheck,
                        isPassword = true,
                        isError = errorMessage != null,
                        placeholder = "비밀번호 확인을 입력해주세요.",
                        onValueChange = {
                            viewModel.onPasswordCheckChanged(it)
                        },
                    )
                    if (errorMessage != null) Text(
                        text = errorMessage!!, color = androidx.compose.ui.graphics.Color.Red
                    )
                }
                Box(
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
//                    SignUpBottomButton(content = "회원가입", onClick = {
//                        focusManager.clearFocus()
//                        viewModel.signUp()
//                    }, enabled = valid)
                    SignUpBottomButton(
                        content = if (isLoading) "가입중..." else "회원가입",
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.signUp()
                        },
                        enabled = valid,
                        isLoading = isLoading
                    )
                }
            }
        }
    }

}

@Composable
fun SignUpBottomButton(
    content: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,  // 로딩 상태 추가
    modifier: Modifier = Modifier
        .height(52.dp)
        .fillMaxWidth()
        .semantics { contentDescription = "$content button" },
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor,
        contentColor = whiteBackgroundColor,
        disabledContentColor = polishedSteelColor,
        disabledContainerColor = greyBackgroundColor
    ),
) {
    Button(
        enabled = enabled, // 로딩 중이면 버튼 비활성화
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = content,
            style = subtitle2.copy(fontWeight = FontWeight.Bold),
            color = whiteBackgroundColor)
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp),
                color = whiteBackgroundColor,
                strokeWidth = 2.dp
            )
        }
    }
}