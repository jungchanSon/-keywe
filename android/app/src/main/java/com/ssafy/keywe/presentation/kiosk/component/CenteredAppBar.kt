package com.ssafy.keywe.presentation.kiosk.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

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

//fun isOnlyCurrentScreenInBackStack(navController: NavController): Boolean {
//    return navController.previousBackStackEntry == null
//}