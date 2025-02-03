package com.ssafy.keywe.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.presentation.auth.viewmodel.SignUpViewModel

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
                    BottomButton(content = "회원가입", onClick = {
                        focusManager.clearFocus()
                        viewModel.signUp()
                    }, enabled = valid)
                }
            }
        }
    }

}
