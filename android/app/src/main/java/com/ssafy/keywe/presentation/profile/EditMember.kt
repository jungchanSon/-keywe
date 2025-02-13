package com.ssafy.keywe.presentation.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.presentation.profile.viewmodel.EditMemberViewModel
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.button
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.primaryColor

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun EditMemberScreen(
    navController: NavController,
    viewModel: EditMemberViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    // 프로필 이미지 선택
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it) }
    }
    val profileId by ProfileIdManager.profileId.collectAsStateWithLifecycle()
    // 기존 프로필 정보 불러오기
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(topBar = {
        DefaultAppBar(title = "구성원 수정", navController = navController, actions = {
            TextButton(onClick = {
                viewModel.updateProfile(profileViewModel)
                navController.navigate(Route.ProfileBaseRoute.ProfileChoiceRoute(false))
            }) {
                Text("완료", color = primaryColor)
            }
        })
    },
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() } // 빈 공간 클릭 시 포커스 제거
            )) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Box(modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 24.dp)
                .clickable { imagePicker.launch("image/*") })
            // 프로필 이미지 섹션
            ProfileImagePicker(viewModel, { imagePicker.launch("image/*") })


            Spacer(modifier = Modifier.height(16.dp))

            // 이름 입력 필드
            DefaultTextFormField(label = "이름",
                placeholder = state.name,
                text = state.name,
                onValueChange = { viewModel.onNameChange(it) })

            Spacer(modifier = Modifier.height(16.dp))

            // 부모일 경우 추가 필드
            if (state.role == "PARENT") {
                PhoneNumberInput(viewModel = viewModel)
                Spacer(modifier = Modifier.height(16.dp))

                DefaultTextFormField(
                    label = "간편 비밀번호",
                    placeholder = "비밀번호 숫자 4자리를 입력해주세요.",
                    text = state.password,
                    onValueChange = { viewModel.onSimplePasswordChange(it) },
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 삭제 버튼
            TextButton(

                onClick = {
                    profileId?.let {
                        AlertDialog.Builder(context).setTitle("프로필 삭제")
                            .setMessage("정말로 이 프로필을 삭제하시겠습니까?").setPositiveButton("삭제") { _, _ ->
                                viewModel.deleteProfile(it)
                                navController.navigateUp() // 삭제 후 이전 화면으로 이동
                            }.setNegativeButton("취소", null).show()
                    }
                }, modifier = Modifier
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

// 📌 프로필 이미지 선택기
@Composable
fun ProfileImagePicker(viewModel: EditMemberViewModel, imagePicker: () -> Unit) {
    Box(modifier = Modifier
        .size(150.dp)
        .padding(vertical = 24.dp)
        .clickable { imagePicker() }) {
        // 기본 프로필 이미지
        Image(
            painter = painterResource(id = R.drawable.humanimage),
            contentDescription = "프로필 이미지",
            modifier = Modifier.fillMaxSize()
//                .align(Alignment.BottomEnd)
        )

        // 수정 버튼 아이콘
        Image(painter = painterResource(id = R.drawable.edit),
            contentDescription = "프로필 수정 버튼",
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.BottomEnd)
                .clickable { imagePicker() })
    }
}

@Composable
fun PhoneNumberInput(viewModel: EditMemberViewModel) {
    val phoneText = viewModel.phoneTextFieldValue.collectAsState().value
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }

    // 포커스 상태 감지
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction: Interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isFocused = true
                is FocusInteraction.Unfocus -> isFocused = false
            }
        }
    }
    Column {
        Text(text = "휴대폰 번호", style = button, modifier = Modifier.padding(bottom = 8.dp))

        OutlinedTextField(
            value = phoneText,
            onValueChange = { viewModel.onPhoneChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            interactionSource = interactionSource,
            isError = !viewModel.state.collectAsState().value.isPhoneValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp), // 기존 필드와 동일한 높이
            placeholder = {
                Text("휴대폰 번호를 입력해주세요", style = body2.copy(color = Color.Gray))
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = greyBackgroundColor,
                unfocusedContainerColor = greyBackgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorContainerColor = greyBackgroundColor
            ),
            textStyle = body2,
            singleLine = true
        )
    }
}

