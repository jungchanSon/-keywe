package com.ssafy.keywe.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.presentation.profile.viewmodel.EditMemberViewModel
import com.ssafy.keywe.ui.theme.body1
import com.ssafy.keywe.ui.theme.button
import com.ssafy.keywe.ui.theme.primaryColor

@Composable
fun EditMember(
    navController: NavController, viewModel: EditMemberViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        DefaultAppBar(title = "구성원 수정", navController = navController, actions = {
            TextButton(onClick = { /* 완료 처리 */ }) {
                Text("완료", color = primaryColor)
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
            ) {
                // 프로필 이미지
                Image(
                    painter = painterResource(id = R.drawable.humanimage),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier.fillMaxSize()
                )

                // 수정하기 버튼
                Image(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "프로필 수정하기 버튼",
                    modifier = Modifier
                        .size(13.dp)
                        .align(Alignment.BottomEnd)
                        .clickable { /*프로필 이미지 수정하기 페이지*/ }
                )
            }

            // 기존 이름 탭, 부모 표시
            Text(
                text = "김동철",  //state.name
                style = body1,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
            // 이름 입력
            DefaultTextFormField(
                label = "이름",
                placeholder = "김동철", //기존에 저장한 이름 넣기
                text = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 휴대폰 번호
            DefaultTextFormField(
                label = "휴대폰 번호",
                placeholder = "***-****-****", //기존에 저장한 핸드폰 번호 넣기
                text = state.simplePassword,
                onValueChange = { viewModel.onSimplePasswordChange(it) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 간편 비밀번호
            DefaultTextFormField(
                label = "간편 비밀번호",
                placeholder = "간편 비밀번호 숫자 4자리를 입력해주세요.",
                text = state.simplePassword,
                onValueChange = { viewModel.onSimplePasswordChange(it) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 프로필 삭제하기 버튼
            TextButton(
                onClick = { /* 프로필 삭제 처리 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "프로필 삭제하기", style = button, textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}
