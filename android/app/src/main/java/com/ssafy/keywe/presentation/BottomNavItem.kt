package com.ssafy.keywe.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.ssafy.keywe.R

sealed class BottomNavItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val screenRoute: String,
) {
    object Home : BottomNavItem(R.string.home, Icons.Default.Home, HomeScreen.toString())
    object Profile :
        BottomNavItem(R.string.profile, Icons.Default.PersonOutline, ProfileScreen.toString())

    object Login :
        BottomNavItem(R.string.app_name, Icons.Default.Apps, LoginScreen.toString())
}