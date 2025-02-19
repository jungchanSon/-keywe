package com.ssafy.keywe.presentation.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.button
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.viewmodel.AddMemberViewModel


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AddMemberScreen(
    navController: NavController,
    viewModel: AddMemberViewModel = hiltViewModel(),
) {
    val backStackEntry =
        navController.getBackStackEntry<Route.ProfileBaseRoute.ProfileChoiceRoute>()
    val isJoinApp = backStackEntry?.arguments?.getBoolean("isJoinApp") ?: false
    val profileViewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

    val state = viewModel.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    val imageUriState by viewModel.profileImageUri.collectAsState()
    val context = LocalContext.current


    //화면열때마다 api 갱신(추가하기 버튼에 해놨지롱)
//    LaunchedEffect(Unit) {
//        viewModel.profileAddedEvent.collect {
//            Log.d("AddMemberScreen", "Add Routing")
//
//            profileViewModel.refreshProfileList() // 추가 후 목록 새로고침
//
//            if (isJoinApp) {
//                navController.navigate(Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp = true))
//                {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = false
//                    }  // 기존 스택을 유지할지 여부 설정, true로 이동하면서 popupto로 스택정리
//                }
//            } else {
//                // false로 popbackstack 사용하여 이동
//                navController.popBackStack(
//                    Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp), false
//                )
//            }
//        }
//    }


    Scaffold(
        topBar = { DefaultAppBar(title = "구성원 추가", navController = navController) },
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() })
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // 프로필 이미지 피커로 교체
            ProfileImagePicker(
                viewModel = viewModel, modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 부모/자녀 토글
            ParentChildToggle(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onTabSelect(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 이름 입력
            DefaultTextFormField(
                label = "이름",
                placeholder = "이름을 입력해주세요.",
                text = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            if (state.selectedTab == 0) { // 부모인 경우
                Spacer(modifier = Modifier.height(16.dp))
                PhoneNumberInput(viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                // 인증번호 입력
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "인증번호",
                            style = button,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (state.isVerificationSent) {
                            Text(
                                text = viewModel.verificationMessage.collectAsState().value,
                                style = body2,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .background(greyBackgroundColor, shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicTextField(
                                value = state.verificationCode,
                                onValueChange = { viewModel.onVerificationCodeChange(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                textStyle = body2,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            if (state.verificationCode.isEmpty()) {
                                Text(
                                    text = "인증번호를 입력해주세요",
                                    style = body2.copy(color = Color.Gray),
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.CenterStart)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (state.isVerificationSent) {
                                    viewModel.verifyCode()
                                } else {
                                    viewModel.sendVerification()
                                }
                            },
                            modifier = Modifier
                                .height(52.dp)
                                .width(140.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            enabled = if (state.isVerificationSent) {
                                viewModel.isVerificationButtonEnabled.collectAsState().value
                            } else {
                                state.isPhoneValid && !state.isVerificationSent
                            }
                        ) {
                            Text(text = if (state.isVerificationSent) "인증확인" else "인증번호전송")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 간편 비밀번호
                DefaultTextFormField(
                    label = "간편 비밀번호",
                    placeholder = "간편 비밀번호 숫자 4자리를 입력해주세요.",
                    text = state.password,
                    onValueChange = { viewModel.onSimplePasswordChange(it) },
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 추가하기 버튼
            Button(
                onClick = {
                    val imageUri = imageUriState
                        ?: Uri.parse("android.resource://${context.packageName}/${R.drawable.humanimage}") // 기본 이미지 적용

                    viewModel.postProfile(
                        context = context,
                        profileRequest = PostProfileRequest(
                            role = if (state.selectedTab == 0) "PARENT" else "CHILD",
                            name = state.name,
                            phone = if (state.selectedTab == 0) state.phone else null,
                            password = if (state.selectedTab == 0) state.password else null
                        ),
                        imageUri = imageUri,
                        onSuccess = {
                            profileViewModel.refreshProfileList() // 추가 후 목록 새로고침

                            if (isJoinApp) {
                                navController.navigate(
                                    Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp = true)
                                ) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = false
                                    }
                                }
                            } else {
                                navController.popBackStack(
                                    Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp), false
                                )
                            }
                        },
                        onError = { errorMessage ->
                            Log.e("AddMemberScreen", " 프로필 추가 실패: $errorMessage")
                        }

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                enabled = viewModel.isAddButtonEnabled.collectAsState().value
            ) {
                Text("추가하기")
            }

//            Button(
//                onClick = {
//                    val imageUri = imageUriState
//                        ?: Uri.parse("android.resource://${context.packageName}/${R.drawable.humanimage}")
//                    if (imageUri != null) {
//                        viewModel.postProfile(
//                            context = context,
//                            onSuccess = {
//                                profileViewModel.refreshProfileList() // 추가 후 목록 새로고침
//
//                                if (isJoinApp) {
//                                    navController.navigate(
//                                        Route.ProfileBaseRoute.ProfileChoiceRoute(
//                                            isJoinApp = true
//                                        )
//                                    ) {
//                                        popUpTo(0) { inclusive = false }
//                                    }
//                                } else {
//                                    navController.popBackStack(
//                                        Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp), false
//                                    )
//                                }
//                            }
//                        )
//                    } else {
//                        Log.e("Profile", "이미지가 선택되지 않았습니다.")
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                enabled = viewModel.isAddButtonEnabled.collectAsState().value
//            ) {
//                Text("추가하기")
//            }

//            Button(
//                onClick = {
//                    viewModel.postProfile(
//                        onSuccess = {
//                            profileViewModel.refreshProfileList() // 추가 후 목록 새로고침
//
//                            if (isJoinApp) {
//                                // profileChoiceScreen(true)로 이동하면서 스택 정리
//                                navController.navigate(
//                                    Route.ProfileBaseRoute.ProfileChoiceRoute(
//                                        isJoinApp = true
//                                    )
//                                ) {
//                                    popUpTo(0) { inclusive = false }
//                                }
//                            } else {
//                                // profileChoiceScreen(false)로 popBackStack() 사용하여 이동
//                                navController.popBackStack(
//                                    Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp), false
//                                )
//                            }
//                        }
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                enabled = viewModel.isAddButtonEnabled.collectAsState().value
//            ) {
//                Text("추가하기")
//            }


//            // 추가하기 버튼
//            Button(
//                onClick = {
//                    viewModel.postProfile(
//                        onSuccess = {
//                            navController.popBackStack(
//                                Route.ProfileBaseRoute.ProfileChoiceRoute(isJoinApp), false
//                            )
//                        }
//                    )
////                    viewModel.postProfile()
////                    val backstack =
////                        navController.getBackStackEntry<Route.ProfileBaseRoute.ProfileAddRoute>()
//
////                    viewModel.postProfile()
//
////                    navController.popBackStack()
////                    backstack.destination
//
////                    Log.d("NavBackStack", "현재화면 : $currentRoute")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                enabled = viewModel.isAddButtonEnabled.collectAsState().value
//            ) {
//                Text("추가하기")
//            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


// 부모, 자녀 토글
@Composable
fun ParentChildToggle(
    selectedTab: Int, onTabSelected: (Int) -> Unit, modifier: Modifier = Modifier,
) {
    val offsetXState = animateFloatAsState(
        targetValue = if (selectedTab == 0) 0f else 1f, label = "Toggle Animation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(greyBackgroundColor, shape = RoundedCornerShape(25.dp))
    ) {
        var width by remember { mutableStateOf(0) }

        Box(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .onSizeChanged { width = it.width }
            .offset {
                IntOffset(
                    x = (offsetXState.value * width).toInt(), y = 0
                )
            }
            .background(primaryColor, shape = RoundedCornerShape(25.dp)))

        Row(
            modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("부모님", "자녀").forEachIndexed { index, option ->
                TextButton(
                    onClick = { onTabSelected(index) }, modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = option,
                        color = if (selectedTab == index) Color.White else Color.Black,
                    )
                }
            }
        }
    }
}


// 핸드폰 번호 입력 필드
@Composable
fun PhoneNumberInput(viewModel: AddMemberViewModel) {
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


@Composable
fun ProfileImagePicker(
    viewModel: AddMemberViewModel, modifier: Modifier = Modifier,
) {
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imageUri by viewModel.profileImageUri.collectAsState()
    val context = LocalContext.current
    val defaultImage = R.drawable.humanimage
    val imageModel = imageUri ?: R.drawable.humanimage
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
//            imageUri = it
            viewModel.updateProfileImage(it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier
            .size(120.dp)
            .clickable { imagePicker.launch("image/*") }) {
            val model: Any = imageUri ?: defaultImage
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUri ?: R.drawable.humanimage)
                    .crossfade(true)
                    .build(),
//                model = imageUri ?: defaultImage,
                contentDescription = "프로필 이미지",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            Image(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "이미지 수정",
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            )
        }
    }
}



