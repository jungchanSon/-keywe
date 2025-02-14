package com.ssafy.keywe.common

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.kiosk.InputPasswordScreen
import com.ssafy.keywe.presentation.kiosk.InputPhoneNumberScreen
import com.ssafy.keywe.presentation.kiosk.KioskHomeScreen
import com.ssafy.keywe.presentation.kiosk.viewmodel.KioskViewModel
import com.ssafy.keywe.presentation.order.MenuCartScreen
import com.ssafy.keywe.presentation.order.MenuDetailScreen
import com.ssafy.keywe.presentation.order.MenuScreen
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuDetailViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.presentation.profile.AddMemberScreen
import com.ssafy.keywe.presentation.profile.EditMemberScreen
import com.ssafy.keywe.presentation.profile.EmailVerification
import com.ssafy.keywe.presentation.profile.ProfileChoiceScreen
import com.ssafy.keywe.presentation.profile.ProfileScreen
import com.ssafy.keywe.webrtc.screen.ConnectingScreen
import com.ssafy.keywe.webrtc.screen.HelperWaitingRoomScreen
import com.ssafy.keywe.webrtc.screen.ParentWaitingRoomScreen
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
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
        data object KioskHomeRoute : Route

        @Serializable
        data object MenuRoute : Route

        @Serializable
        data class MenuDetailRoute(val id: Long) : Route

        @Serializable
        data object MenuCartRoute : Route

        @Serializable
        data object KioskPhoneNumberRoute : Route

        @Serializable
        data object KioskPasswordRoute : Route

        @Serializable
        data class HelperWaitingRoomRoute(
            val storeId: String,
            val kioskUserId: String,
            val sessionId: String,
        ) : Route

        @Serializable
        data object ParentWaitingRoomRoute : Route

        @Serializable
        data object ConnectingScreenRoute : Route
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

        @Serializable
        data object ProfileScreenRoute : Route
    }

//    @Serializable
//    data object KioskBaseRoute : Route {
//
//        @Serializable
//        data object KioskPhoneNumberRoute : Route
//
//        @Serializable
//        data object KioskPasswordRoute : Route
//    }
}

sealed interface BottomRoute {
    @Serializable
    data object ProfileRoute : BottomRoute

    @Serializable
    data object HomeRoute : BottomRoute
}

@Serializable
object SharingRoute

@Serializable
data class HelperRoute(val isKiosk: Boolean = false, val channelName: String)

@Serializable
data object LoginRoute

@Serializable
object SignUpRoute


//@Serializable
//data class WaitingRoomRoute(
//    val storeId: String,
//    val kioskUserId: String,
//    val sessionId: String,
//)

fun NavGraphBuilder.profileGraph(navController: NavHostController, tokenManager: TokenManager) {
    navigation<Route.ProfileBaseRoute>(startDestination = BottomRoute.ProfileRoute) {
        composable<BottomRoute.ProfileRoute> { ProfileScreen(navController, tokenManager) }
        composable<Route.ProfileBaseRoute.ProfileChoiceRoute> {
            val args = it.toRoute<Route.ProfileBaseRoute.ProfileChoiceRoute>()
            ProfileChoiceScreen(navController, args.isJoinApp)
        }
        composable<Route.ProfileBaseRoute.ProfileEditRoute> { EditMemberScreen(navController) }
        composable<Route.ProfileBaseRoute.ProfileEmailVerifyRoute> { EmailVerification(navController) }
        composable<Route.ProfileBaseRoute.ProfileAddRoute> { AddMemberScreen(navController) }
        composable<Route.ProfileBaseRoute.ProfileScreenRoute> {
            ProfileScreen(
                navController, tokenManager
            )
        }
    }
}

fun NavGraphBuilder.menuGraph(
    navController: NavHostController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel,
    keyWeViewModel: KeyWeViewModel,
) {
    navigation<Route.MenuBaseRoute>(startDestination = Route.MenuBaseRoute.KioskHomeRoute) {
        composable<Route.MenuBaseRoute.KioskHomeRoute> {
            KioskHomeScreen(navController, menuCartViewModel, appBarViewModel)
        }
        composable<Route.MenuBaseRoute.MenuRoute> {
            val menuScreenViewModel: MenuViewModel = hiltViewModel()
            MenuScreen(
                navController,
                menuScreenViewModel,
                menuCartViewModel,
                appBarViewModel,
                keyWeViewModel
            )
        }
        composable<Route.MenuBaseRoute.MenuDetailRoute> {
            val menuDetailViewModel: MenuDetailViewModel = hiltViewModel()
            val arg = it.toRoute<Route.MenuBaseRoute.MenuDetailRoute>()
            Log.d("Detail Navigator", ":$arg.id")
            MenuDetailScreen(
                navController,
                menuDetailViewModel,
                menuCartViewModel,
                appBarViewModel,
                arg.id,
                keyWeViewModel
            )
        }
        composable<Route.MenuBaseRoute.MenuCartRoute> {
            MenuCartScreen(navController, menuCartViewModel, appBarViewModel, keyWeViewModel)
        }
        composable<Route.MenuBaseRoute.KioskPhoneNumberRoute> {
            val kioskViewModel: KioskViewModel = hiltViewModel()
            InputPhoneNumberScreen(
                navController, menuCartViewModel, appBarViewModel, kioskViewModel
            )
        }
        composable<Route.MenuBaseRoute.KioskPasswordRoute> {
            val kioskViewModel: KioskViewModel = hiltViewModel()
            InputPasswordScreen(
                navController, menuCartViewModel, appBarViewModel, kioskViewModel
            )
        }

        composable<Route.MenuBaseRoute.HelperWaitingRoomRoute> {
            val arg = it.toRoute<Route.MenuBaseRoute.HelperWaitingRoomRoute>()
            HelperWaitingRoomScreen(
                navController,
                arg.sessionId,
                arg.storeId,
                arg.kioskUserId,
                keyWeViewModel = keyWeViewModel
            )
        }

        composable<Route.MenuBaseRoute.ParentWaitingRoomRoute> {
            val arg = it.toRoute<Route.MenuBaseRoute.ParentWaitingRoomRoute>()
            ParentWaitingRoomScreen(
                navController, keyWeViewModel = keyWeViewModel
            )
        }

        composable<Route.MenuBaseRoute.ConnectingScreenRoute> {
            ConnectingScreen(
                navController, menuCartViewModel, appBarViewModel
            )
        }
    }
}
