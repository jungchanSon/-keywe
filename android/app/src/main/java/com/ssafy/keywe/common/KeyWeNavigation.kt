package com.ssafy.keywe.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.kiosk.InputPhoneNumberScreen
import com.ssafy.keywe.presentation.order.MenuCartScreen
import com.ssafy.keywe.presentation.order.MenuDetailScreen
import com.ssafy.keywe.presentation.order.MenuScreen
import com.ssafy.keywe.presentation.profile.AddMemberScreen
import com.ssafy.keywe.presentation.profile.EditMember
import com.ssafy.keywe.presentation.profile.EmailVerification
import com.ssafy.keywe.presentation.profile.ProfileChoiceScreen
import com.ssafy.keywe.presentation.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Composable
fun currentBottomRoute(navController: NavHostController): BottomRoute? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route // string
    return route?.let {
        return when (route.split(".").last()) {
            BottomRoute.HomeRoute::class.simpleName -> BottomRoute.HomeRoute
            BottomRoute.ProfileRoute::class.simpleName -> BottomRoute.ProfileRoute
            else -> null
        }
    }
}

@Serializable
object SplashRoute


sealed interface Route {
    @Serializable
    data object MenuBaseRoute : Route {
        @Serializable
        data object MenuRoute : Route

        @Serializable
        data class MenuDetailRoute(val id: Int) : Route

        @Serializable
        data object MenuCartRoute : Route
    }

    @Serializable
    data object ProfileBaseRoute : Route {

        @Serializable
        data class ProfileChoiceRoute(val isJoinApp: Boolean) : Route

        @Serializable
        data object ProfileEditRoute : Route

        @Serializable
        data object ProfileAddRoute : Route

        @Serializable
        data object ProfileEmailVerifyRoute : Route
    }

    @Serializable
    data object KioskBaseRoute : Route {

        @Serializable
        data object KioskPhoneNumberRoute : Route

        @Serializable
        data object KioskPasswordRoute : Route
    }
}

sealed interface BottomRoute {
    @Serializable
    data object ProfileRoute : BottomRoute

    @Serializable
    data object HomeRoute : BottomRoute
}

@Serializable
data object LoginRoute

@Serializable
object SignUpRoute


fun NavGraphBuilder.profileGraph(navController: NavHostController, tokenManager: TokenManager) {
    navigation<Route.ProfileBaseRoute>(startDestination = BottomRoute.ProfileRoute) {
        composable<BottomRoute.ProfileRoute> { ProfileScreen(navController, tokenManager) }
        composable<Route.ProfileBaseRoute.ProfileChoiceRoute> {
            val args = it.toRoute<Route.ProfileBaseRoute.ProfileChoiceRoute>()
            ProfileChoiceScreen(navController, args.isJoinApp)
        }
        composable<Route.ProfileBaseRoute.ProfileEditRoute> { EditMember(navController) }
        composable<Route.ProfileBaseRoute.ProfileEmailVerifyRoute> { EmailVerification(navController) }
        composable<Route.ProfileBaseRoute.ProfileAddRoute> { AddMemberScreen(navController) }
    }
}

fun NavGraphBuilder.menuGraph(navController: NavHostController) {
    navigation<Route.MenuBaseRoute>(startDestination = Route.MenuBaseRoute.MenuRoute) {
        composable<Route.MenuBaseRoute.MenuRoute> { MenuScreen(navController) }
        composable<Route.MenuBaseRoute.MenuDetailRoute> {
            val arg = it.toRoute<Route.MenuBaseRoute.MenuDetailRoute>()
            MenuDetailScreen(navController, arg.id)
        }
        composable<Route.MenuBaseRoute.MenuCartRoute> { MenuCartScreen(navController) }
    }
}

fun NavGraphBuilder.kioskGraph(navController: NavHostController) {
    navigation<Route.KioskBaseRoute>(startDestination = Route.KioskBaseRoute.KioskPhoneNumberRoute) {
        composable<Route.KioskBaseRoute.KioskPhoneNumberRoute> {
            InputPhoneNumberScreen(
                navController
            )
        }
    }
}
