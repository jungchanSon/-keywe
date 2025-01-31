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
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.keywe.common.app.BottomButton
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

    val navController = rememberNavController()
    Scaffold(topBar = {
//        DefaultAppBar(title = "AAA", navController = navController)
        TopAppBar(title = { Text("title") })

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
                        placeholder = "이메일을 입력해주세요.",
                        onValueChange = {
                            viewModel.onEmailChanged(it)

                        },
                    )
                    Spacer(modifier.height(12.dp))
                    DefaultTextFormField(
                        label = "비밀번호",
                        text = password,
                        placeholder = "비밀번호를 입력해주세요.",
                        onValueChange = {
                            viewModel.onPasswordChanged(it)

                        },
                    )
                    Spacer(modifier.height(12.dp))
                    DefaultTextFormField(
                        label = "비밀번호 확인",
                        text = passwordCheck,
                        placeholder = "비밀번호 확인을 입력해주세요.",
                        onValueChange = {
                            viewModel.onPasswordCheckChanged(it)

                        },
                    )
                }
                Box(
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    BottomButton(content = "회원가입", onClick = {
                        focusManager.clearFocus()
                        viewModel.signUp()
                    })
                }
            }
        }
    }

}
