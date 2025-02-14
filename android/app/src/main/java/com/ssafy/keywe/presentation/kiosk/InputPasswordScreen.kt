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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ssafy.keywe.ui.theme.formFillColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.titleTextColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InputPasswordScreen(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel = hiltViewModel(),
    kioskViewModel: KioskViewModel = hiltViewModel(),
) {
    val inputPassword by kioskViewModel.inputPassword.collectAsStateWithLifecycle()

    val isCertificationNumberValid: Boolean = (inputPassword.length == 4)

    LaunchedEffect(Unit) {
        kioskViewModel.clearInputPassword() // 화면이 새로 열릴 때 초기화
    }

    Scaffold(topBar = {
        CenteredAppBar(
            title = "키위 매칭",
            navController = navController
        )
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
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
                // 인증번호 입력 필드
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
                        navController.navigate(
                            Route.MenuBaseRoute.ParentWaitingRoomRoute
                        )
                    }
                }, onBackClick = {
                    navController.popBackStack()
                }, isConfirmEnabled = inputPassword.length == 4
                )
            }
        }
    }
}