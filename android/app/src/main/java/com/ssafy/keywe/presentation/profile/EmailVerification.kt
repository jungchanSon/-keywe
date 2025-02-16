package com.ssafy.keywe.presentation.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.h6sb


//@Composable
//fun EmailVerification(
//    navController: NavController,
//    viewModel: EmailVerificationViewModel = hiltViewModel()
//) {
//    val state = viewModel.state.collectAsState().value
//    Scaffold(
//        topBar = {
//            DefaultAppBar(
//                title = "이메일 인증",
//                navController = navController
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 24.dp)
//        ) {
//            // 이메일 입력
//            DefaultTextFormField(
//                label = "이메일",
//                placeholder = "이메일을 입력해주세요.",
//                text = state.email,
//                onValueChange = { viewModel.onEmailChange(it) },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            // 인증번호 입력
//            Column {
//                if (state.isVerificationSent) {
//                    Text(
//                        text = "인증번호가 발송되었습니다.",
//                        style = body2,
//                        color = primaryColor,
//                        modifier = Modifier.align(Alignment.End)
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    DefaultTextFormField(
//                        label = "인증번호",
//                        placeholder = "인증번호를 입력해주세요",
//                        text = state.verificationCode,
//                        onValueChange = { viewModel.onVerificationCodeChange(it) },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp)
//                    )
//                    Button(
//                        onClick = { viewModel.sendVerification() },
//                        modifier = Modifier
//                            .height(48.dp)
//                            .align(Alignment.Bottom),
//                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                        enabled = state.isEmailValid && !state.isVerificationSent
//                    ) {
//                        Text("확인")
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // 확인 버튼
//            Button(
//                onClick = {
////                     viewModel.verify()
//                },
//
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                enabled = state.isVerificationValid
//            ) {
//                Text("확인")
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}

@Composable
fun EmailVerificationScreen(
    navController: NavHostController,
    email: String,
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("이메일 인증 필요", style = h6sb)
        Spacer(modifier = Modifier.height(16.dp))
        Text("$email 로 인증 이메일이 발송되었습니다.\n이메일을 확인하고 인증을 완료하세요.", style = caption)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { resendVerificationEmail(email) }) {
            Text("인증 이메일 재전송")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate(Route.AuthBaseRoute.LoginRoute) }) {
            Text("로그인 페이지로 돌아가기")
        }
    }
}

fun resendVerificationEmail(email: String) {
    // ✅ 이메일 인증 요청 API 호출 (예: /auth/user/send-verification)
    Log.d("EmailVerification", "인증 이메일 재전송 요청: $email")
}
