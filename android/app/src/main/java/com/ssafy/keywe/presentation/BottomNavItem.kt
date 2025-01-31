package com.ssafy.keywe.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.ssafy.keywe.R
import com.ssafy.keywe.common.BottomRoute

sealed class BottomNavItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val screenRoute: BottomRoute,
) {
    data object Home : BottomNavItem(R.string.home, Icons.Default.Home, BottomRoute.HomeRoute)
    data object Profile :
        BottomNavItem(R.string.profile, Icons.Default.PersonOutline, BottomRoute.ProfileRoute)

    data object Login : BottomNavItem(R.string.app_name, Icons.Default.Apps, BottomRoute.LoginRoute)
}