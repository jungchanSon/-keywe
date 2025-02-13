package com.ssafy.keywe.webrtc.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssafy.keywe.common.BottomRoute
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.data.websocket.SignalService
import com.ssafy.keywe.data.websocket.SignalType
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun connectSTOMP(context: Context) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.CONNECT.name
    context.startService(intent)
}

private fun subscribeSTOMP(context: Context, profileId: String) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.SUBSCRIBE.name
    intent.putExtra(
        "profileId", profileId
    )
    context.startService(intent)
}

@Composable
fun WaitingRoomScreen(
    navController: NavHostController,
    sessionId: String,
    storeId: String,
    kioskUserId: String,
    viewModel: SignalViewModel = hiltViewModel(),
) {
    // 초기 값은 null일 수 있으므로 안전하게 처리
    val message by viewModel.stompMessageFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (message != null) {
        Text(text = "Received message: ${message}")
    } else {
        Text(text = "No message yet")
    }
    var text = "연결 중 입니다."

    val scope = rememberCoroutineScope()

    LaunchedEffect(message) {
        message?.let {
            when (it.type) {
                STOMPTYPE.REQUESTED -> {
                    // 연결 완료 후 구독
                    subscribeSTOMP(context, "677367955509677381")
                    Log.d("WaitingRoomScreen", "구독 중입니다.")
                    text = "구독 중입니다."
                }

                STOMPTYPE.WAITING -> {
                    Log.d(
                        "WaitingRoomScreen", "알림 ${it.data!!.success}개 성공 ${it.data!!.failure}개 실패."
                    )
                    text = "알림을 보냈습니다."
                }

                STOMPTYPE.ACCEPTED -> {
                    scope.launch {
                        delay(3000)
                    }

                    navController.navigate(Route.MenuBaseRoute.MenuRoute) {
                        navBackStackEntry?.destination?.route?.let { route ->
                            popUpTo(route) {
                                inclusive = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                STOMPTYPE.TIMEOUT -> {

                }

                STOMPTYPE.END -> {

                }

                STOMPTYPE.ERROR -> {

                }
            }
        }

    }

    LaunchedEffect(Unit) {
        connectSTOMP(context)
    }


    Scaffold(topBar = {
        DefaultAppBar(
            title = "대기방", navController = navController
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding), verticalArrangement = Arrangement.Center
        ) {
            Text("$text")
            CountdownTimer(navController)
        }

    }
}

@Composable
fun CountdownTimer(navController: NavHostController) {
    var seconds by remember { mutableIntStateOf(3) }

    LaunchedEffect(Unit) {
        while (seconds > 0) {
            delay(1000) // 1초 대기
            seconds--  // 초 감소
        }
    }
    LaunchedEffect(seconds) {
        if (seconds == 0) {
            Log.d("CountdownTimer", "타이머 종료")
            navController.navigate(BottomRoute.HomeRoute, builder = {
                popUpTo(BottomRoute.HomeRoute) {
                    inclusive = true
                }
            })
        }
    }

    Text(
        text = if (seconds > 0) "${seconds}초 후에 00이 됩니다." else "00",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}
