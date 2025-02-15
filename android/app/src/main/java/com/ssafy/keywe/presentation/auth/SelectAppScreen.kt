package com.ssafy.keywe.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.BottomButton

@Composable
fun SelectAppScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding), verticalArrangement = Arrangement.Center) {
            BottomButton(content = "사장님으로 시작하기", onClick = {
                navController.navigate(
                    Route.AuthBaseRoute.CeoLoginRoute
                )
            })
            Spacer(modifier = Modifier.height(16.dp))
            BottomButton(content = "사용자로 시작하기", onClick = {
                navController.navigate(
                    Route.AuthBaseRoute.LoginRoute
                )
            })
        }
    }
}