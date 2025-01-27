package com.ssafy.keywe.common.app

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.ssafy.keywe.ui.theme.h6
import timber.log.Timber

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navController: NavController,
) {
    LaunchedEffect(navController.currentBackStackEntry) {
        navController.currentBackStackEntry?.destination?.route?.let {
            Log.d("DefaultAppBar", "Current route: $it")
        }
    }
    val backStackEntries = navController.currentBackStack.value
    Timber.tag("BackStack").d("Back Stack Entries:")
    backStackEntries.forEachIndexed { index, entry ->
        val destination = entry.destination
        val route = destination.route
        val arguments = entry.arguments
        val id = entry.id
        Timber.tag("BackStack")
            .d("  [$index] Destination: $destination, Route: $route, Arguments: $arguments, id: $id")
    }
    CenterAlignedTopAppBar(title = { Text(text = title, style = h6) }, navigationIcon = {
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

        navController.previousBackStackEntry?.let {

        }

    }, actions = actions
    )


}

fun printBackStack(navController: NavController) {

}

fun isOnlyCurrentScreenInBackStack(navController: NavController): Boolean {
    return navController.previousBackStackEntry == null
}