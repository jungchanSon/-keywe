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
    abstract val name: String

    data object Home : BottomNavItem(R.string.home, Icons.Default.Home, BottomRoute.HomeRoute) {
        override val name: String = "홈"
    }

    data object Profile :
        BottomNavItem(R.string.profile, Icons.Default.PersonOutline, BottomRoute.ProfileRoute) {
        override val name: String = "프로필"
    }

    data object Login :
        BottomNavItem(R.string.app_name, Icons.Default.Apps, BottomRoute.LoginRoute) {
        override val name: String = "로그인"
    }
}