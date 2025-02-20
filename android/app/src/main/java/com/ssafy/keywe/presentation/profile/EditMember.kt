package com.ssafy.keywe.presentation.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.primaryColor
import java.io.ByteArrayInputStream

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun EditMemberScreen(
    navController: NavController,
    viewModel: EditMemberViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // 프로필 이미지 선택
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(uri) }  //이미지 uri 업데이트
    }
    val profileId by ProfileIdManager.profileId.collectAsStateWithLifecycle()
    // 기존 프로필 정보 불러오기
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(topBar = {
        DefaultAppBar(title = "프로필 수정", navController = navController, actions = {
            TextButton(onClick = {
                viewModel.updateProfile(
                    context, profileViewModel, navController
                )
                navController.navigate(Route.ProfileBaseRoute.ProfileScreenRoute) {
                    popUpTo(Route.ProfileBaseRoute.ProfileEditRoute) { inclusive = true }
                }
            }) {
                Text("완료", color = primaryColor)
            }
        })
    },
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable{ focusManager.clearFocus() } // 빈 공간 클릭 시 포커스 제거
            ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 프로필 이미지 선택 및 표시
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
                PhoneNumberInput(
                    phone = state.phone, // ✅ 기존에 저장된 핸드폰 번호를 전달
                    onPhoneChange = { viewModel.onPhoneChange(it) }, // ✅ 핸드폰 번호 변경 함수 전달
                    isPhoneValid = state.isPhoneValid
                )
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
                        Log.d("profileId", "$profileId")
                        AlertDialog.Builder(context).setTitle("프로필 삭제")
                            .setMessage("정말로 이 프로필을 삭제하시겠습니까?").setPositiveButton("삭제") { _, _ ->
                                viewModel.deleteProfile(it, navController)

//                                navController.navigateUp() // 삭제 후 이전 화면으로 이동
                            }.setNegativeButton("취소", null).show()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
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
    val selectImageUri by viewModel.profileImageUri.collectAsStateWithLifecycle()
    val existingImageState by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .size(150.dp)
            .padding(vertical = 24.dp)
            .noRippleClickable { imagePicker() },
        contentAlignment = Alignment.Center
    ) {
        when {
            selectImageUri != null -> {
                Image(
                    painter = rememberAsyncImagePainter(selectImageUri),
                    contentDescription = "선택한 프로필 이미지",
                    modifier = Modifier.fillMaxSize()
                )
            }

            !existingImageState.profileImage.isNullOrBlank() -> {
                val bitmap = decodeBase64ToBitmap(existingImageState.profileImage!!)
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "프로필 이미지",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Log.e("ProfileImagePicker", "비트맵 변환 실패")
                    Image(
                        painter = painterResource(id = R.drawable.humanimage),
                        contentDescription = "기본 프로필 이미지",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            else -> {
                Image(
                    painter = painterResource(id = R.drawable.humanimage),
                    contentDescription = "기본 프로필 이미지",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        // 수정 버튼 아이콘
        Image(painter = painterResource(id = R.drawable.edit),
            contentDescription = "프로필 수정 버튼",
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.BottomEnd)
                .noRippleClickable { imagePicker() })
    }
}

@Composable
fun PhoneNumberInput(
    phone: String, // ✅ 기존에 저장된 핸드폰 번호를 불러오도록 수정
    onPhoneChange: (String) -> Unit, // ✅ 핸드폰 번호 변경 함수 전달
    isPhoneValid: Boolean, // ✅ 유효성 검사 상태 전달
) {
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
            value = phone, // ✅ 기존에 저장된 핸드폰 번호 표시
            onValueChange = { onPhoneChange(it) }, // ✅ 값이 변경될 때 onPhoneChange 호출
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            interactionSource = interactionSource,
            isError = !isPhoneValid, // ✅ 유효성 검사 반영
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

@Composable
private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
    } catch (e: IllegalArgumentException) {
        null
    }
}

