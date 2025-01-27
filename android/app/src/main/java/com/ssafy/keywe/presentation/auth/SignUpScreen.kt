package com.ssafy.keywe.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.presentation.auth.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
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
                    label = "이름",
                    text = viewModel.name.value,
                    placeholder = "이름을 입력해주세요.",
                    onValueChange = {},
                )
                Spacer(modifier.height(12.dp))
                DefaultTextFormField(
                    label = "이메일",
                    text = viewModel.email.value,
                    placeholder = "이메일을 입력해주세요.",
                    onValueChange = {},
                )
                Spacer(modifier.height(12.dp))
                DefaultTextFormField(
                    label = "비밀번호",
                    text = viewModel.password.value,
                    placeholder = "비밀번호를 입력해주세요.",
                    onValueChange = {},
                )
                Spacer(modifier.height(12.dp))
                DefaultTextFormField(
                    label = "비밀번호 확인",
                    text = viewModel.passwordCheck.value,
                    placeholder = "비밀번호 확인을 입력해주세요.",
                    onValueChange = {},
                )

            }
            Box(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                BottomButton(content = "회원가입", onClick = {})

            }
        }
    }
}
