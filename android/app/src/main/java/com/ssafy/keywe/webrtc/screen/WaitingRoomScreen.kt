package com.ssafy.keywe.webrtc.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.app.DefaultAppBar

@Composable
fun WaitingRoomScreen(
    navController: NavHostController,
    sessionId: String,
    storeId: String,
    kioskUserId: String,
) {
    Scaffold(topBar = {
        DefaultAppBar(
            title = "대기방",
            navController = navController
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding), verticalArrangement = Arrangement.Center
        ) {
            Text("대기방입니다.")
        }

    }
}