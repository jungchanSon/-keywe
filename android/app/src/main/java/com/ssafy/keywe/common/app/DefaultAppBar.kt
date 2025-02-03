package com.ssafy.keywe.common.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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