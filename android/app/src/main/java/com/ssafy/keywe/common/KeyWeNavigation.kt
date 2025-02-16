package com.ssafy.keywe.common

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.auth.CeoLoginScreen
import com.ssafy.keywe.presentation.auth.LoginScreen
import com.ssafy.keywe.presentation.auth.SelectAppScreen
import com.ssafy.keywe.presentation.auth.LoginScreen
import com.ssafy.keywe.presentation.kiosk.InputPasswordScreen
import com.ssafy.keywe.presentation.kiosk.InputPhoneNumberScreen
import com.ssafy.keywe.presentation.kiosk.KioskHomeScreen
import com.ssafy.keywe.presentation.order.MenuCartScreen
import com.ssafy.keywe.presentation.order.MenuDetailScreen
import com.ssafy.keywe.presentation.order.MenuScreen
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuDetailViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.presentation.profile.AddMemberScreen
import com.ssafy.keywe.presentation.profile.EditMemberScreen
import com.ssafy.keywe.presentation.profile.EmailVerificationScreen
import com.ssafy.keywe.presentation.profile.ProfileChoiceScreen
import com.ssafy.keywe.presentation.profile.ProfileScreen
import com.ssafy.keywe.webrtc.screen.HelperWaitingRoomScreen
import com.ssafy.keywe.webrtc.screen.ParentWaitingRoomScreen
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel
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
        data class MenuRoute(val storeId: Long) : Route

        @Serializable
        data class MenuDetailRoute(val id: Long) : Route

        @Serializable
        data class MenuCartRoute(val storeId: Long) : Route

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

    @Serializable
    data object AuthBaseRoute : Route {
        @Serializable
        data object SelectAppRoute

        @Serializable
        data object LoginRoute

        @Serializable
        data object CeoLoginRoute

    }

    companion object {
        // ✅ 문자열 기반의 EmailVerificationRoute 추가
        fun EmailVerificationRoute(email: String): String {
            return "email_verification?email=$email"
        }

        fun LoginRoute(): String {
            return "login"
        }
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
data object PermissionRoute

@Serializable
object SignUpRoute

//@Serializable
//data class EmailVerificationRoute(val email: String)


//@Serializable
//data class WaitingRoomRoute(
//    val storeId: String,
//    val kioskUserId: String,
//    val sessionId: String,
//)

@SuppressLint("NewApi")
fun NavGraphBuilder.authGraph(naveController: NavHostController, tokenManager: TokenManager) {
    navigation<Route.AuthBaseRoute>(startDestination = Route.AuthBaseRoute.SelectAppRoute) {
        composable<Route.AuthBaseRoute.SelectAppRoute> { SelectAppScreen(navController = naveController) }
        composable<Route.AuthBaseRoute.LoginRoute> { LoginScreen(navController = naveController) }
        composable<Route.AuthBaseRoute.CeoLoginRoute> { CeoLoginScreen(navController = naveController) }

    }

}


fun NavGraphBuilder.profileGraph(navController: NavHostController, tokenManager: TokenManager) {
//fun NavGraphBuilder.profileGraph(
//    navController: NavHostController,
//    tokenManager: TokenManager,
////    profileDataStore: ProfileDataStore
//) {
//    navigation<Route.ProfileBaseRoute>(startDestination = BottomRoute.ProfileRoute) {
//        composable<BottomRoute.ProfileRoute> { ProfileScreen(navController, tokenManager) }
//        composable<Route.ProfileBaseRoute.ProfileChoiceRoute> {
//            val args = it.toRoute<Route.ProfileBaseRoute.ProfileChoiceRoute>()
//            val profileDataStore: ProfileDataStore = hiltViewModel()
//            ProfileChoiceScreen(navController, args.isJoinApp)
//        }
//        composable<Route.ProfileBaseRoute.ProfileEditRoute> { EditMemberScreen(navController) }
////        composable<Route.ProfileBaseRoute.ProfileEmailVerifyRoute> {
////            EmailVerificationScreen(
////                navController
////            )
//        }
//        composable<Route.ProfileBaseRoute.ProfileAddRoute> { AddMemberScreen(navController) }
//        composable<Route.ProfileBaseRoute.ProfileScreenRoute> {
//            ProfileScreen(
//                navController, tokenManager
//            )
//        }
//
//    }
//}

fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    tokenManager: TokenManager
) {
    navigation<Route.ProfileBaseRoute>(startDestination = BottomRoute.ProfileRoute) {
        composable<BottomRoute.ProfileRoute> { ProfileScreen(navController, tokenManager) }
        composable<Route.ProfileBaseRoute.ProfileChoiceRoute> {
            val args = it.toRoute<Route.ProfileBaseRoute.ProfileChoiceRoute>()
            ProfileChoiceScreen(navController, args.isJoinApp)
        }
        composable<Route.ProfileBaseRoute.ProfileEditRoute> { EditMemberScreen(navController) }
        composable<Route.ProfileBaseRoute.ProfileAddRoute> { AddMemberScreen(navController) }
        composable<Route.ProfileBaseRoute.ProfileScreenRoute> {
            ProfileScreen(
                navController,
                tokenManager
            )
        }

        // ✅ 이메일 인증 화면 추가
        composable("email_verification?email={email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EmailVerificationScreen(navController, email)
        }
        composable("login") {
            LoginScreen(navController)
        }
    }
}

fun NavGraphBuilder.menuGraph(
    navController: NavHostController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel,
    signalViewModel: SignalViewModel,
    tokenManager: TokenManager,
) {
    // navigation()을 사용하여 menuGraph라는 그래프의 route를 "menuGraph"로 지정합니다.
    navigation<Route.MenuBaseRoute>(startDestination = Route.MenuBaseRoute.KioskHomeRoute) {
        composable<Route.MenuBaseRoute.KioskHomeRoute> {
//            val kioskViewModel: KioskViewModel = hiltViewModel() // ✅ 한 번만 생성
            KioskHomeScreen(navController, tokenManager)
//            KioskHomeScreen(navController, menuCartViewModel, appBarViewModel, kioskViewModel)
        }
        composable<Route.MenuBaseRoute.MenuRoute> {
            val arg = it.toRoute<Route.MenuBaseRoute.MenuRoute>()

            val menuScreenViewModel: MenuViewModel = hiltViewModel()
            // 여기서는 상위에서 전달받은 viewModel을 그대로 사용합니다.
            MenuScreen(
                navController, menuScreenViewModel, menuCartViewModel, appBarViewModel,
                // 아래에서 menuGraph 범위에 한정된 keyWeViewModel을 생성하는 방법을 적용합니다.
                getScopedKeyWeViewModel(navController), signalViewModel, tokenManager, arg.storeId
            )
        }
        composable<Route.MenuBaseRoute.MenuDetailRoute> {
            val menuDetailViewModel: MenuDetailViewModel = hiltViewModel()
            val arg = it.toRoute<Route.MenuBaseRoute.MenuDetailRoute>()
            MenuDetailScreen(
                navController,
                menuDetailViewModel,
                menuCartViewModel,
                appBarViewModel,
                arg.id,
                getScopedKeyWeViewModel(navController),
                signalViewModel,
                tokenManager
            )
        }
        composable<Route.MenuBaseRoute.MenuCartRoute> {
            val arg = it.toRoute<Route.MenuBaseRoute.MenuCartRoute>()
            MenuCartScreen(
                navController,
                menuCartViewModel,
                appBarViewModel,
                getScopedKeyWeViewModel(navController),
                signalViewModel,
                tokenManager,
                arg.storeId
            )
        }
        // 나머지 destination도 동일하게 getScopedKeyWeViewModel(navController)를 사용
        composable<Route.MenuBaseRoute.HelperWaitingRoomRoute> {
            val arg = it.toRoute<Route.MenuBaseRoute.HelperWaitingRoomRoute>()
            HelperWaitingRoomScreen(
                navController,
                arg.sessionId,
                arg.storeId,
                arg.kioskUserId,
                signalViewModel,
                getScopedKeyWeViewModel(navController),
                tokenManager
            )
        }
        composable<Route.MenuBaseRoute.ParentWaitingRoomRoute> {
            ParentWaitingRoomScreen(
                navController, signalViewModel, getScopedKeyWeViewModel(navController), tokenManager
            )
        }

        composable<Route.MenuBaseRoute.KioskPhoneNumberRoute> {
            InputPhoneNumberScreen(navController, menuCartViewModel, appBarViewModel)
        }
        composable<Route.MenuBaseRoute.KioskPasswordRoute> {
            InputPasswordScreen(navController, menuCartViewModel, appBarViewModel)
        }
    }
}

// 별도의 함수로 menuGraph 범위에 한정된 keyWeViewModel을 생성하는 예시입니다.
@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun getScopedKeyWeViewModel(navController: NavHostController): KeyWeViewModel {
    // "menuGraph"라는 route로 지정한 네비게이션 그래프의 BackStackEntry를 가져옵니다.
    val parentEntry = remember { navController.getBackStackEntry(Route.MenuBaseRoute) }
    // 이 BackStackEntry를 스코프로 하여 ViewModel을 생성하면, menuGraph 내에서만 생존하게 됩니다.
    return hiltViewModel(parentEntry)
}
