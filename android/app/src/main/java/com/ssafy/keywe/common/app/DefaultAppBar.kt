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
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import io.agora.rtc2.Constants

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun DefaultAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navController: NavController? = null,
) {
    TopAppBar(
        backgroundColor = whiteBackgroundColor,
        elevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = { Text(text = title, style = h6) },
        navigationIcon = {
            if (navController != null && !isOnlyCurrentScreenInBackStack(navController)) {
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

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun DefaultOrderAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navController: NavController,
    viewModel: OrderAppBarViewModel = hiltViewModel(),
    keyWeViewModel: KeyWeViewModel,
    isRoot: Boolean = false,
    isKiosk: Boolean = false,
) {
//    val speakerSound by viewModel.speakerSound.collectAsState()
//    val isKiWiMatching by viewModel.isKiWiMatching.collectAsState()
    val audioRoute by keyWeViewModel.audioRoute.collectAsStateWithLifecycle()

    TopAppBar(backgroundColor = whiteBackgroundColor,
        elevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = { Text(text = title, style = h6) },
        navigationIcon = {
            if (!isOnlyCurrentOrderScreenInBackStack(navController)) {
                IconButton(
                    modifier = Modifier
                        .then(if (isKiosk) {
                            Modifier.pointerInteropFilter { true }
                        } else {
                            Modifier
                        })
                        .semantics {
                            contentDescription = "back_button"
                        },
                    onClick = {
                        if (isRoot) viewModel.openDialog()
                        else navController.popBackStack()
                        if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.BackButton)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = if (!isKiosk) Color.Black else Color.Transparent,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {
                keyWeViewModel.toggleAudio()
            }) {
                Icon(
                    imageVector = if (audioRoute == Constants.AUDIO_ROUTE_SPEAKERPHONE) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.Headset,
                    contentDescription = if (audioRoute == Constants.AUDIO_ROUTE_SPEAKERPHONE) "스피커폰" else "이어폰"
                )
            }
            IconButton(onClick = {
                viewModel.openDialog()
            }) {
                Icon(
                    imageVector = Icons.Filled.PhoneDisabled, // 예제 아이콘
                    contentDescription = "통화 끊기"
                )
            }
            actions() // 기존에 전달된 actions도 추가
        })
}

fun isOnlyCurrentOrderScreenInBackStack(navController: NavController): Boolean {
    val isCurrentDestination = try {
        navController.currentDestination == navController.getBackStackEntry<Route.MenuBaseRoute.MenuRoute>().destination
    } catch (e: IllegalArgumentException) {
        false
    }
    return isCurrentDestination
}