package com.ssafy.keywe.presentation.kiosk

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.kiosk.component.CenteredAppBar
import com.ssafy.keywe.presentation.kiosk.component.KeypadScreen
import com.ssafy.keywe.presentation.kiosk.viewmodel.KioskViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.formFillColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.titleTextColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InputPasswordScreen(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel = hiltViewModel(),
) {
    val parentEntry = remember(navController) {
        navController.previousBackStackEntry // ✅ 이전 화면의 ViewModel 가져오기
    }
    val kioskViewModel: KioskViewModel = hiltViewModel(parentEntry!!)

    val inputPassword by kioskViewModel.inputPassword.collectAsStateWithLifecycle()
    val verificationError by kioskViewModel.verificationError.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        kioskViewModel.clearInputPassword() // 화면이 새로 열릴 때 초기화
    }

    Scaffold(topBar = {
        CenteredAppBar(
            title = "키위 매칭",
            navController = navController
        )
    }) { innerPadding ->
        Box(modifier = Modifier
            .padding(top = 50.dp)
//            .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            ) {
                Box(
                    modifier = Modifier
                        .height(75.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(top = 40.dp, bottom = 10.dp),
                        text = "인증 번호를 입력해주세요.",
                        style = h6.copy(letterSpacing = 0.sp)
                    )
                }

                Box(
                    modifier = Modifier
                        .height(30.dp) // 고정된 높이 설정
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val errorText = verificationError // 로컬 변수로 저장
                    if (errorText != null) {
                        Text(
                            text = errorText,
                            style = caption,
                            color = Color.Red,
                            modifier = Modifier.padding(
//                                vertical = 8.dp,
                                horizontal = 24.dp
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.height(30.dp)) // 같은 높이의 빈 공간 유지
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (0..3).forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .background(formFillColor, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when {
                                    index < inputPassword.length - 1 -> "*"
                                    index == inputPassword.length - 1 -> inputPassword.lastOrNull()
                                        ?.toString() ?: ""

                                    else -> "-"
                                }, fontSize = 24.sp, color = titleTextColor
                            )
                        }
                        if (index < 3) Spacer(modifier = Modifier.width(12.dp))
                    }
                }

                KeypadScreen(onNumberClick = { number ->
                    if (number == "Backspace") return@KeypadScreen
                    if (inputPassword.length < 4) {
                        kioskViewModel.updateInputPassword(inputPassword + number)
                    }
                }, onBackspaceClick = {
                    if (inputPassword.isNotEmpty()) {
                        kioskViewModel.updateInputPassword(inputPassword.dropLast(1))
                    }
                }, onConfirmClick = {
                    if (inputPassword.length == 4) {
                        kioskViewModel.verifyUser(navController)
                    }
                }, onBackClick = {
                    navController.popBackStack()
                }, isConfirmEnabled = inputPassword.length == 4
                )
            }
        }
    }
}