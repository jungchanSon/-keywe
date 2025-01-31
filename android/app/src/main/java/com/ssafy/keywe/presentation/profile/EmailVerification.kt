package com.ssafy.keywe.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.presentation.profile.viewmodel.EmailVerificationViewModel
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.primaryColor


@Composable
fun EmailVerification(
    navController: NavController,
    viewModel: EmailVerificationViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    Scaffold(
        topBar = {
            DefaultAppBar(
                title = "이메일 인증",
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            // 이메일 입력
            DefaultTextFormField(
                label = "이메일",
                placeholder = "이메일을 입력해주세요.",
                text = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // 인증번호 입력
            Column {
                if (state.isVerificationSent) {
                    Text(
                        text = "인증번호가 발송되었습니다.",
                        style = body2,
                        color = primaryColor,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DefaultTextFormField(
                        label = "인증번호",
                        placeholder = "인증번호를 입력해주세요",
                        text = state.verificationCode,
                        onValueChange = { viewModel.onVerificationCodeChange(it) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Button(
                        onClick = { viewModel.sendVerification() },
                        modifier = Modifier
                            .height(48.dp)
                            .align(Alignment.Bottom),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        enabled = state.isEmailValid && !state.isVerificationSent
                    ) {
                        Text("확인")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 확인 버튼
            Button(
                onClick = {
//                     viewModel.verify()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                enabled = state.isVerificationValid
            ) {
                Text("확인")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
