package com.ssafy.keywe.presentation.kiosk

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.kiosk.component.CenteredAppBar
import com.ssafy.keywe.presentation.kiosk.component.KeypadScreen
import com.ssafy.keywe.presentation.kiosk.component.PhoneNumberDisplay
import com.ssafy.keywe.presentation.kiosk.viewmodel.KioskViewModel
import com.ssafy.keywe.presentation.order.component.MenuCartDeleteDialog
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.formFillColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.kiosk.component.NoTitleTwoButtonDialog

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InputPhoneNumberScreen(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel = hiltViewModel(),
) {

    val kioskViewModel: KioskViewModel = hiltViewModel()

    var phoneNumber1 by remember { mutableStateOf("") }
    var phoneNumber2 by remember { mutableStateOf("") }
    var phoneNumber3 by remember { mutableStateOf("") }

    val isCheckProfileDialogOpen by kioskViewModel.isCheckProfileDialogOpen.collectAsStateWithLifecycle()
    val phoneNumber by kioskViewModel.phoneNumber.collectAsStateWithLifecycle()

    val isPhoneNumberValid =
        phoneNumber1.length == 3 && phoneNumber2.length == 4 && phoneNumber3.length == 4

//    LaunchedEffect(isCheckProfileDialogOpen) {
//        if (!isCheckProfileDialogOpen) {
//        }
//    }

    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(
                color = if (isCheckProfileDialogOpen) titleTextColor.copy(
                    alpha = 0.5f
                ) else Color.Transparent
            )
    ) {
        // 개별 삭제 다이얼로그
        if (isCheckProfileDialogOpen) {
            NoTitleTwoButtonDialog(description = "김노인 님이 맞습니까?\n$phoneNumber",
                onCancel = { kioskViewModel.closeCheckProfileDialog() },
                onConfirm = {
                    kioskViewModel.closeCheckProfileDialog()
                    navController.navigate(Route.MenuBaseRoute.KioskPasswordRoute)
                })
        }

    }

    Scaffold(topBar = { CenteredAppBar(title = "키위 매칭", navController = navController) }
    ) { innerPadding ->
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
                        text = "본인 휴대폰 번호를 입력해주세요.",
                        style = h6.copy(letterSpacing = 0.sp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                PhoneNumberDisplay(phoneNumber1, phoneNumber2, phoneNumber3)

                KeypadScreen(onNumberClick = { number ->
                    if (number == "Backspace") return@KeypadScreen

                    if (phoneNumber1.length < 3) {
                        phoneNumber1 += number
                    } else if (phoneNumber2.length < 4) {
                        phoneNumber2 += number
                    } else if (phoneNumber3.length < 4) {
                        phoneNumber3 += number
                    }
                }, onBackspaceClick = {
                    when {
                        phoneNumber3.isNotEmpty() -> phoneNumber3 = phoneNumber3.dropLast(1)
                        phoneNumber2.isNotEmpty() -> phoneNumber2 = phoneNumber2.dropLast(1)
                        phoneNumber1.isNotEmpty() -> phoneNumber1 = phoneNumber1.dropLast(1)
                    }
                }, onConfirmClick = {
                    if (isPhoneNumberValid) {
                        kioskViewModel.clearInputPassword()
                        val fullNumber = "${phoneNumber1}${phoneNumber2}${phoneNumber3}"

                        kioskViewModel.updatePhoneNumber(fullNumber)  // 그냥 호출 가능
                        navController.navigate(Route.MenuBaseRoute.KioskPasswordRoute)  // 업데이트 후 화면 이동
                    }

                }, onBackClick = {
                    navController.popBackStack()
                }, isConfirmEnabled = isPhoneNumberValid
                )
            }
        }
    }
}