package com.ssafy.keywe.ui.profile

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.R
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.button
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.viewmodel.AddMemberViewModel


@Composable
fun AddMember(
    viewModel: AddMemberViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = { DefaultAppBar(title = "구성원 추가") },
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
            SwitchableTab(
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
                .animateContentSize()
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
//fun AddMember(
//    viewModel: AddMemberViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
//) {
//    var name by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var verificationCode by remember { mutableStateOf("") }
//    var selectedTab by remember { mutableStateOf(0) }
//    val state by viewModel.state.collectAsState()
//
//    Scaffold(
//        topBar = { DefaultAppBar(title = "구성원 추가") },
//        modifier = Modifier.fillMaxSize()
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(horizontal = 24.dp)
//                .fillMaxSize()
//        ) {
//            // 프로필 이미지
//            Image(
//                painter = painterResource(id = R.drawable.humanimage),
//                contentDescription = "프로필 이미지",
//                modifier = Modifier
//                    .size(120.dp)
//                    .align(Alignment.CenterHorizontally)
//                    .padding(24.dp)
//            )
//
//            // 탭 선택 버튼
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp)
//                    .background(greyBackgroundColor, RoundedCornerShape(8.dp))
//
//            ) {
//                TabButton(
//                    text = "부모님",
//                    isSelected = selectedTab == 0,
//                    onClick = { selectedTab = 0 },
//                    modifier = Modifier.weight(1f)
//                )
//                TabButton(
//                    text = "자식",
//                    isSelected = selectedTab == 1,
//                    onClick = { selectedTab = 1 },
//                    modifier = Modifier.weight(1f)
//                )
//            }
//
//            // 입력 폼
//            DefaultTextFormField(
//                label = "이름",
//                placeholder = "이름을 입력해주세요.",
//                text = name,
//                onValueChange = { name = it },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            // 휴대폰 번호 입력 부분
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    if (state.isVerificationSent) {
//                        Text(
//                            text = "인증번호가 발송되었습니다.",
//                            style = body2,
//                            color = primaryColor
//                        )
//                    }
//                }
////                ThousandFormatTextField(
////
////                )
//                DefaultTextFormField(
//                    label = "휴대폰 번호",
//                    placeholder = "휴대폰 번호를 입력해주세요.",
//                    text = state.phone,
//                    onValueChange = { viewModel.onPhoneChange(it) },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Number,
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//
//                )
//
//                // 휴대폰 인증번호 입력 부분
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.Bottom
//                ) {
//                    DefaultTextFormField(
//                        label = "인증번호",
//                        placeholder = "인증번호를 입력해주세요",
//                        text = state.verificationCode,
//                        onValueChange = { viewModel.onVerificationCodeChange(it) },
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.NumberPassword
//                        ),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Button(
//                        onClick = { viewModel.sendVerification() },
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .height(55.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                        enabled = state.isPhoneValid
//                    ) {
//                        Text("확인")
//                    }
//                }
//            }
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                DefaultTextFormField(
//                    label = "간편 비밀번호",
//                    placeholder = "간편 비밀번호 숫자 4자리를 입력해주세요.",
//                    text = verificationCode,
//                    onValueChange = {
//                        if (it.length <= 4 && it.all { char -> char.isDigit() }) {
//                            verificationCode = it
//                        }
//                    },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Number  // 숫자 키패드 표시
//                    ),
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // 하단 버튼
//        Button(
//            onClick = { /* 추가하기 처리 */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
//        ) {
//            Text("추가하기")
//        }
//    }
//}
//
//
//@Composable
//fun TabButton(
//    text: String,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .fillMaxHeight()
//            .clickable(onClick = onClick)
//            .background(
//                if (isSelected) primaryColor else Color.Transparent,
//                RoundedCornerShape(8.dp)
//            )
//            .padding(horizontal = 16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = if (isSelected) Color.White else Color.Black,
//            style = button
//        )
//    }
//}button


// GPT버전
//@Composable
//fun AddMemberScreen(
//    onBackClick: () -> Unit,
//    viewModel: AddMemberViewModel = viewModel()
//) {
//    var name by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var verificationCode by remember { mutableStateOf("") }
//    var simplePassword by remember { mutableStateOf("") }
//    var selectedTab by remember { mutableStateOf(0) }
//    var isVerificationSent by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            DefaultAppBar(
//                title = { Text("구성원 추가") },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, "뒤로가기")
//                    }
//                },
//                backgroundColor = Color.White
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(horizontal = 20.dp)
//        ) {
//            ProfileImage()
//            MemberTypeTab(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
//            InputFields(
//                name = name,
//                phone = phone,
//                verificationCode = verificationCode,
//                simplePassword = simplePassword,
//                onNameChange = { name = it },
//                onPhoneChange = { phone = it },
//                onVerificationCodeChange = { verificationCode = it },
//                onSimplePasswordChange = { simplePassword = it },
//                isVerificationSent = isVerificationSent,
//                onVerificationRequest = { isVerificationSent = true }
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            AddButton(enabled = isFormValid(name, phone, verificationCode, simplePassword))
//        }
//    }
//}
//
//@Composable
//private fun ProfileImage() {
//    Image(
//        painter = painterResource(id = R.drawable.default_profile),
//        contentDescription = "프로필 이미지",
//        modifier = Modifier
//            .size(120.dp)
//            .padding(vertical = 24.dp)
//            .clip(CircleShape)
//    )
//}
//
//@Composable
//private fun MemberTypeTab(
//    selectedTab: Int,
//    onTabSelected: (Int) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp)
//            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
//    ) {
//        TabButton(
//            text = "부모님",
//            isSelected = selectedTab == 0,
//            onClick = { onTabSelected(0) },
//            modifier = Modifier.weight(1f)
//        )
//        TabButton(
//            text = "자식",
//            isSelected = selectedTab == 1,
//            onClick = { onTabSelected(1) },
//            modifier = Modifier.weight(1f)
//        )
//    }
//}
//
//@Composable
//private fun InputFields(
//    name: String,
//    phone: String,
//    verificationCode: String,
//    simplePassword: String,
//    onNameChange: (String) -> Unit,
//    onPhoneChange: (String) -> Unit,
//    onVerificationCodeChange: (String) -> Unit,
//    onSimplePasswordChange: (String) -> Unit,
//    isVerificationSent: Boolean,
//    onVerificationRequest: () -> Unit
//) {
//    CustomTextField(
//        value = name,
//        onValueChange = onNameChange,
//        label = "이름",
//        placeholder = "이름을 입력해주세요"
//    )
//
//    PhoneVerificationField(
//        phone = phone,
//        onPhoneChange = onPhoneChange,
//        verificationCode = verificationCode,
//        onVerificationCodeChange = onVerificationCodeChange,
//        isVerificationSent = isVerificationSent,
//        onVerificationRequest = onVerificationRequest
//    )
//
//    PasswordField(
//        password = simplePassword,
//        onPasswordChange = onSimplePasswordChange
//    )
//}
//
//@Composable
//private fun CustomTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    placeholder: String,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.caption,
//            color = Color.Gray
//        )
//        TextField(
//            value = value,
//            onValueChange = onValueChange,
//            placeholder = { Text(placeholder) },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = keyboardOptions,
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color(0xFFF5F5F5),
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
//        )
//    }
//}
//
//@Composable
//private fun AddButton(enabled: Boolean) {
//    Button(
//        onClick = { /* 추가 로직 */ },
//        enabled = enabled,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp),
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = Color(0xFFFF5722),
//            contentColor = Color.White
//        )
//    ) {
//        Text("추가하기")
//    }
//}
