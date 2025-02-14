package com.ssafy.keywe.common.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.PhoneDisabled
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun DefaultAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navController: NavController,
) {
    TopAppBar(
        backgroundColor = whiteBackgroundColor,
        elevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = { Text(text = title, style = h6) },
        navigationIcon = {
            if (!isOnlyCurrentScreenInBackStack(navController)) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
            }
        },
        actions = actions
    )
}

fun isOnlyCurrentScreenInBackStack(navController: NavController): Boolean {
    return navController.previousBackStackEntry == null
}

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun DefaultOrderAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navController: NavController,
    viewModel: OrderAppBarViewModel = hiltViewModel()
) {
    val speakerSound by viewModel.speakerSound.collectAsState()

    TopAppBar(
        backgroundColor = whiteBackgroundColor,
        elevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = { Text(text = title, style = h6) },
        navigationIcon = {
            if (!isOnlyCurrentOrderScreenInBackStack(navController)) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { viewModel.toggleSpeaker() }) {
                Icon(
                    imageVector = if (speakerSound) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.Headset,
                    contentDescription = if (speakerSound) "스피커폰" else "이어폰"
                )
            }
            IconButton(onClick = {
                viewModel.toggleUnconnect()
            }) {
                Icon(
                    imageVector = Icons.Filled.PhoneDisabled, // 예제 아이콘
                    contentDescription = "통화 끊기"
                )
            }
            actions() // 기존에 전달된 actions도 추가
        }
    )
}

fun isOnlyCurrentOrderScreenInBackStack(navController: NavController): Boolean {
    return navController.previousBackStackEntry == null
}