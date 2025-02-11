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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.ui.theme.formFillColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InputPhoneNumberScreen(navController: NavHostController) {
    Scaffold(topBar = { DefaultAppBar(title = "프로필", navController = navController) }
//        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
//                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            ) {
                Box(
                    modifier = Modifier
                        .height(75.dp)
//                        .padding(top = 40.dp, bottom = 10.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(modifier = Modifier.padding(top = 40.dp, bottom = 10.dp),
                        text = "본인 휴대폰 번호를 입력해주세요.", style = h6.copy(letterSpacing = 0.sp)
                    )
                }

                Box(
                    modifier = Modifier
                        .height(86.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier
                            .size(64.dp, 62.dp)
                            .background(color = formFillColor, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = "010", style = h6)
                        }
                        Box(modifier = Modifier
                            .size(8.dp, 1.dp)
                            .background(color = titleTextColor)
                        ){}
                        Box(modifier = Modifier
                            .size(92.dp, 62.dp)
                            .background(color = formFillColor, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = "1234", style = h6)
                        }
                        Box(modifier = Modifier
                            .size(8.dp, 1.dp)
                            .background(color = titleTextColor)
                        ){}
                        Box(modifier = Modifier
                            .size(92.dp, 62.dp)
                            .background(color = formFillColor, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = "5678", style = h6)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun CenteredAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navController: NavController,
) {
    TopAppBar(backgroundColor = whiteBackgroundColor,
        elevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Text(text = title, style = h6)
            }
        },
//        navigationIcon = {
//            if (!isOnlyCurrentScreenInBackStack(navController)) {
//                IconButton(onClick = {
//                    navController.popBackStack()
//                }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
//                        contentDescription = null
//                    )
//                }
//            }
//        },
        actions = {
//            Spacer(modifier = Modifier.fillMaxWidth()) // 오른쪽 여백을 균형 맞추기 위해 추가
        })
}

fun isOnlyCurrentScreenInBackStack(navController: NavController): Boolean {
    return navController.previousBackStackEntry == null
}