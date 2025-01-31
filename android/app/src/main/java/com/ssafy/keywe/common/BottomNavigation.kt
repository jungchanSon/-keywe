package com.ssafy.keywe.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationDefaults.windowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssafy.keywe.presentation.BottomNavItem
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import timber.log.Timber


@Composable
fun MyBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Home, BottomNavItem.Profile, BottomNavItem.Login
    )

    val currentRoute = currentBottomRoute(navController)

//    Log.d("route", "$currentRoute")
    AnimatedVisibility(
        visible = currentRoute != null // items.map { it.screenRoute }.contains(currentRoute)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .background(
                whiteBackgroundColor, shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
            )
            .border(0.dp, Color.Transparent)
            .shadow(
                elevation = 18.dp,
                spotColor = Color(0x0D000000),
                ambientColor = Color(0x0D000000)
            )

//                .defaultMinSize(minHeight = NavigationBarHeight)
            .selectableGroup(),
//            horizontalArrangement = Arrangement.spacedBy(NavigationBarItemHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,


            content = {
                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.hasRoute(screen.screenRoute::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,// currentRoute == item.screenRoute,
                        colors = NavigationBarItemColors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            selectedIndicatorColor = whiteBackgroundColor,
                            unselectedIconColor = titleTextColor,
                            unselectedTextColor = titleTextColor,
                            disabledIconColor = titleTextColor,
                            disabledTextColor = titleTextColor,
                        ),
                        label = {
                            Text(
                                text = stringResource(id = screen.title), style = TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                        },
                        icon = {
                            Icon(
                                screen.icon, contentDescription = stringResource(id = screen.title)
                            )
                        },
                        onClick = {
                            navController.navigate(screen.screenRoute) {
                                navBackStackEntry?.destination?.route?.let {
                                    Timber.d("entry = $it")
                                    popUpTo(it) {
                                        inclusive = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            })

//        NavigationBar(
//            modifier = modifier
//                .border(
//                    border = BorderStroke(0.dp, Color.Black),
//                    shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
//                )
//                .background(
//                    shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
//                    color = primaryColor
//                ),
//            tonalElevation = 10.dp,
//            containerColor = pri,
//            contentColor = primaryColor,
//        ) {
//            items.forEach { item ->
//                NavigationBarItem(
//                    selected = currentRoute == item.screenRoute,
//                    colors = NavigationBarItemColors(
//                        selectedIconColor = primaryColor,
//                        selectedTextColor = primaryColor,
//                        selectedIndicatorColor = whiteBackgroundColor,
//                        unselectedIconColor = titleTextColor,
//                        unselectedTextColor = titleTextColor,
//                        disabledIconColor = titleTextColor,
//                        disabledTextColor = titleTextColor,
//                    ),
//                    label = {
//                        Text(
//                            text = stringResource(id = item.title), style = TextStyle(
//                                fontSize = 12.sp
//                            )
//                        )
//                    },
//                    icon = {
//                        Icon(
//                            item.icon, contentDescription = stringResource(id = item.title)
//                        )
//                    },
//                    onClick = {
//                        navController.navigate(item.screenRoute) {
//                            navBackStackEntry?.destination?.route?.let {
//                                Timber.d("entry = $it")
//                                popUpTo(it) {
//                                    inclusive = true
//                                }
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
//                )
//            }
//        }
    }
}