package com.ssafy.keywe.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.button
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.viewmodel.AddMemberViewModel


@Composable
fun AddMemberScreen(
    navController: NavController,
    viewModel: AddMemberViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = { DefaultAppBar(title = "구성원 추가", navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // 프로필 이미지
            Image(
                painter = painterResource(id = R.drawable.humanimage),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
            )

            // 탭 선택 버튼
//            SwitchableTab(
//                selectedTab = state.selectedTab,
//                onTabSelected = { viewModel.onTabSelect() },
//                modifier = Modifier.fillMaxWidth()
//            )

            Spacer(modifier = Modifier.height(24.dp))

            // 이름 입력
            DefaultTextFormField(
                label = "이름",
                placeholder = "이름을 입력해주세요.",
                text = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 휴대폰 번호 영역
            Column {
                if (state.isVerificationSent) {
                    Text(
                        text = "인증번호가 발송되었습니다.",
                        style = body2,
                        color = primaryColor,
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                DefaultTextFormField(
                    label = "휴대폰 번호",
                    placeholder = "휴대폰 번호를 입력해주세요.",
                    text = state.phone,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                PhoneNumberInput(
                    phoneNumber = state.phone,
                    onPhoneNumberChange = { viewModel.onPhoneChange(it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 인증번호 입력
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        .align(Alignment.Bottom)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    enabled = state.isPhoneValid && !state.isVerificationSent
                ) {
                    Text("확인")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 간편 비밀번호
            DefaultTextFormField(
                label = "간편 비밀번호",
                placeholder = "간편 비밀번호 숫자 4자리를 입력해주세요.",
                text = state.simplePassword,
                onValueChange = { viewModel.onSimplePasswordChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 추가하기 버튼
            Button(
                onClick = { viewModel.addMember() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("추가하기")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

//@Composable
//fun SwitchableTab(
//    selectedTab: Int,
//    onTabSelected: (Int) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(48.dp)
//            .background(greyBackgroundColor, RoundedCornerShape(8.dp))
//            .padding(4.dp)
//    ) {
//        // 슬라이딩 배경
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.5f)
//                .fillMaxHeight()
//                .background(primaryColor, RoundedCornerShape(6.dp))
//                .align(if (selectedTab == 0) Alignment.CenterStart else Alignment.CenterEnd)
//                .animateContentSize()
//        )
//
//        // 탭 버튼들
//        Row(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .clickable { onTabSelected(0) },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "부모님",
//                    color = if (selectedTab == 0) Color.White else Color.Black,
//                    style = button
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .clickable { onTabSelected(1) },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "자식",
//                    color = if (selectedTab == 1) Color.White else Color.Black,
//                    style = button
//                )
//            }
//        }
//    }
//}

//핸드폰 커서 순서 수정 중
@Composable
fun PhoneNumberInput(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
) {
    val numbersOnly = phoneNumber.filter { it.isDigit() }
    val formatted = when {
        numbersOnly.length <= 3 -> numbersOnly
        numbersOnly.length <= 7 -> "${numbersOnly.take(3)}-${numbersOnly.substring(3)}"
        else -> "${numbersOnly.take(3)}-${numbersOnly.substring(3, 7)}-${numbersOnly.substring(7)}"
    }

    BasicTextField(
        value = formatted,
        onValueChange = {
            val cleanInput = it.filter { char -> char.isDigit() }
            if (cleanInput.length <= 11) {
                onPhoneNumberChange(cleanInput)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textStyle = TextStyle(fontSize = 18.sp)
    )
}


//@Composable
//fun AddMember(
//    navController: NavController, viewModel: EditMemberViewModel = hiltViewModel()
//) {
//    val state = viewModel.state.collectAsState().value
//
//    Scaffold(
////        topBar = { DefaultAppBar(title = "구성원 추가") },
//        modifier = Modifier.fillMaxSize()
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(horizontal = 24.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(140.dp)
//                    .align(Alignment.CenterHorizontally)
//                    .padding(vertical = 24.dp)
//            ) {
//                // 프로필 이미지
//                Image(
//                    painter = painterResource(id = R.drawable.humanimage),
//                    contentDescription = "프로필 이미지",
//                    modifier = Modifier.fillMaxSize()
//                )
//
//                // 수정 아이콘
//                Image(
//                    painter = painterResource(id = R.drawable.edit),
//                    contentDescription = "프로필 사진 수정",
//                    modifier = Modifier
//                        .size(13.dp)
//                        .align(Alignment.BottomEnd)
//                        .clickable { /*프로필이미지수정으로*/ }
//                )
//            }
//
//            // 탭 선택 버튼
//            SwitchableTab(
//                selectedTab = state.selectedTab,
//                onTabSelected = { viewModel.onTabSelect(it) },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // 이름 입력
//            DefaultTextFormField(
//                label = "이름",
//                placeholder = "이름을 입력해주세요.",
//                text = state.name,
//                onValueChange = { viewModel.onNameChange(it) },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 휴대폰 번호 영역
//            Column(
//                verticalArrangement = Arrangement.spacedBy(0.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(text = "휴대폰 번호", style = body2)
//                    if (state.isVerificationSent) {
//                        Text(
//                            text = if (state.verificationStatus == VerificationStatus.SUCCESS) {
//                                "인증 성공"
//                            } else {
//                                "인증 번호 발송"
//                            },
//                            style = body2,
//                        )
//                    }
//                }
//                DefaultTextFormField(
//                    label = "",
//                    placeholder = "휴대폰 번호를 입력해주세요.",
//                    text = state.phone,
//                    onValueChange = { viewModel.onPhoneChange(it) },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 인증번호 입력
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.Bottom
//            ) {
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                ) {
//                    Text(text = "인증번호", style = body2)
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(52.dp)
//                            .background(greyBackgroundColor)
//                    ) {
//
//
//                        BasicTextField(
//                            value = state.verificationCode,
//                            onValueChange = {
//                                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
//                                    viewModel.onVerificationCodeChange(it)
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp, vertical = 12.dp),
//                            textStyle = body2,
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            singleLine = true
//                        )
//                        if (state.verificationCode.isEmpty()) {
//                            Box(
//                                modifier = Modifier.fillMaxHeight(),  // 높이를 채움
//                                contentAlignment = Alignment.Center  // 내용물을 중앙 정렬
//                            ) {
//                                Text(
//                                    text = "인증번호를 입력해주세요",
//                                    style = body2,
//                                    color = Color.Gray,
//                                    modifier = Modifier.padding(
//                                        horizontal = 16.dp
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//
//                Button(
//                    onClick = {
//                        if (!state.isVerificationSent) {
//                            viewModel.sendVerification()
//                        } else {
//                            viewModel.verifyCode()
//                        }
//                    },
//                    modifier = Modifier
//                        .height(52.dp)
//                        .width(80.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                    enabled = if (!state.isVerificationSent) {
//                        state.isPhoneValid
//                    } else {
//                        state.verificationCode.length == 6
//                    }
//                ) {
//                    Text(
//                        text = if (!state.isVerificationSent) "전송" else "인증 확인"
//                    )
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 간편 비밀번호
//            DefaultTextFormField(
//                label = "간편 비밀번호",
//                placeholder = "간편 비밀번호 숫자 4자리를 입력해주세요.",
//                text = "",
//                onValueChange = { viewModel.onSimplePasswordChange(it) },
//                isPassword = true,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // 추가하기 버튼
//            Button(
//                onClick = { viewModel.addMember() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
//            ) {
//                Text("추가하기")
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}

//슬라이드형 아직 연구 중
//@Composable
//fun SwitchableTab(
//    selectedTab: Int,
//    onTabSelected: (Int) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(48.dp)
//            .background(greyBackgroundColor, RoundedCornerShape(8.dp))
//            .padding(4.dp)
//    ) {
//        // 슬라이딩 배경에 애니메이션 추가
//        val transition = updateTransition(targetState = selectedTab, label = "tab")
//        val offsetX = transition.animateFloat(
//            label = "offsetX",
//            transitionSpec = {
//                spring(
//                    dampingRatio = Spring.DampingRatioMediumBouncy,
//                    stiffness = Spring.StiffnessLow
//                )
//            }
//        ) { tab ->
//            when (tab) {
//                0 -> 0f
//                else -> 1f
//            }
//        }.value
//
//        // 슬라이딩 배경
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.5f)
//                .fillMaxHeight()
//                .background(primaryColor, RoundedCornerShape(6.dp))
//                .align(Alignment.CenterStart)
//                .offset(x = (offsetX * (modifier.fillMaxWidth().width - modifier.fillMaxWidth(0.5f).width)).dp)
//        )
//
//        // 탭 버튼들
//        Row(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .clickable { onTabSelected(0) },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "부모님",
//                    color = if (selectedTab == 0) Color.White else Color.Black,
//                    style = button
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .clickable { onTabSelected(1) },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "자식",
//                    color = if (selectedTab == 1) Color.White else Color.Black,
//                    style = button
//                )
//            }
//        }
//    }
//}


//기존 탭형 부모자식 선택
@Composable
fun SwitchableTab(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(greyBackgroundColor, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        // 슬라이딩 배경
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .background(primaryColor, RoundedCornerShape(6.dp))
                .align(if (selectedTab == 0) Alignment.CenterStart else Alignment.CenterEnd)
        )

        // 탭 버튼들
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected(0) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "부모님",
                    color = if (selectedTab == 0) Color.White else Color.Black,
                    style = button
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected(1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "자식",
                    color = if (selectedTab == 1) Color.White else Color.Black,
                    style = button
                )
            }
        }
    }
}

//@Composable
//fun PhoneNumberInputScreen() {
//    var phoneNumber by remember { mutableStateOf("") }
//
//    DefaultTextFormField(
//        label = "핸드폰 번호",
//        placeholder = "010-1234-5678",
//        text = phoneNumber,
//        onValueChange = { phoneNumber = it }, // 실시간 값 갱신
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
////핸드폰 커서 순서 수정 중
//@Composable
//fun PhoneNumberInput(
//    phoneNumber: String,
//    onPhoneNumberChange: (String) -> Unit
//) {
//    val numbersOnly = phoneNumber.filter { it.isDigit() }
//    val formatted = when {
//        numbersOnly.length <= 3 -> numbersOnly
//        numbersOnly.length <= 7 -> "${numbersOnly.take(3)}-${numbersOnly.substring(3)}"
//        else -> "${numbersOnly.take(3)}-${numbersOnly.substring(3, 7)}-${numbersOnly.substring(7)}"
//    }
//
//    BasicTextField(
//        value = formatted,
//        onValueChange = {
//            val cleanInput = it.filter { char -> char.isDigit() }
//            if (cleanInput.length <= 11) {
//                onPhoneNumberChange(cleanInput)
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        textStyle = TextStyle(fontSize = 18.sp)
//    )
//}

