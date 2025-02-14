package com.ssafy.keywe.webrtc.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.websocket.SignalService
import com.ssafy.keywe.data.websocket.SignalType
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
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

private fun acceptSTOMP(context: Context, sessionId: String) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.ACCEPT.name
    intent.putExtra(
        "sessionId", sessionId
    )
    context.startService(intent)
}

@Composable
fun HelperWaitingRoomScreen(
    navController: NavHostController,
    sessionId: String,
    storeId: String,
    kioskUserId: String,
    viewModel: SignalViewModel = hiltViewModel(),
    keyWeViewModel: KeyWeViewModel = hiltViewModel(),
) {
    // 초기 값은 null일 수 있으므로 안전하게 처리
    val message by viewModel.stompMessageFlow.collectAsStateWithLifecycle()
    val connected by viewModel.connected.collectAsStateWithLifecycle()
    val subscribe by viewModel.subscribed.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val profileId by ProfileIdManager.profileId.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val isLoading by remember { mutableStateOf(true) }

    if (message != null) {
        Text(text = "Received message: ${message}")
    } else {
        Text(text = "No message yet")
    }
    var text = "연결 중 입니다."

    val rtcConnected by keyWeViewModel.connected.collectAsState()


    LaunchedEffect(rtcConnected) {
        if (!rtcConnected) {
            navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(Unit) {
        connectSTOMP(context)
    }

    LaunchedEffect(connected) {
        if (connected) {
            // 연결 후 구독
            subscribeSTOMP(context, profileId.toString())
        }

    }

    LaunchedEffect(subscribe) {
        if (subscribe) {
            Log.d("Accept", "Accept = $sessionId")
            acceptSTOMP(context, sessionId)
        }
    }

    LaunchedEffect(message) {
        if (profileId != null) {
            message?.let {
                when (it.type) {
                    STOMPTYPE.SUBSCRIBE -> {
                        // 연결 완료 후 구독
                        text = "구독 중입니다."
                    }

                    STOMPTYPE.REQUESTED -> {
                        Log.d(
                            "WaitingRoomScreen",
                            "요청 ${it.data!!.success}개 성공 ${it.data!!.failure}개 실패."
                        )
                        text = "요청을 보냈습니다."
                    }

                    STOMPTYPE.WAITING -> {
                        Log.d(
                            "WaitingRoomScreen",
                            "알림 ${it.data!!.success}개 성공 ${it.data!!.failure}개 실패."
                        )
                        text = "알림을 보냈습니다."
                    }

                    STOMPTYPE.ACCEPTED -> {
                        val sessionId = it.data!!.sessionId!!
                        val helperUserId = it.data.helperUserId
                        val kioskUserId = it.data.kioskUserId
                        val channel = it.data.channel!!
                        keyWeViewModel.connectWebRTC()
                        keyWeViewModel.joinChannel(channel)
                        keyWeViewModel.remoteStats.collect { state ->
                            if (state != null) {
                                Log.d("WaitingRoomScreen", "connect remote")
                                navController.navigate(Route.MenuBaseRoute.MenuRoute)
                            } else {
                                Log.d("WaitingRoomScreen", "state is null")
                            }
                        }
                    }

                    STOMPTYPE.TIMEOUT -> {

                    }

                    STOMPTYPE.END -> {
                        Log.d("WaitingRoomScreen", "종료")
                        closeSTOMP(context)
                        keyWeViewModel.exit()
                    }

                    STOMPTYPE.ERROR -> {
                        if (it.data!!.code == "R100") {
                            // 만료된 주문 요청입니다
                            Toast.makeText(context, "만료된 주문 요청입니다.", Toast.LENGTH_LONG).show()
                            Log.d("WaitingRoomScreen", "만료된 주문 요청입니다.")

                        } else if (it.data.code == "R101") {
                            // 가족에게 요청을 보내지 못했어요.
                            Toast.makeText(context, "가족에게 요청을 보내지 못했어요.", Toast.LENGTH_LONG).show()
                            Log.d("WaitingRoomScreen", "가족에게 요청을 보내지 못했어요.")
                        } else {
                            // 자식 프로필은 주문 요청이 불가합니다.
                            Toast.makeText(context, "자식 프로필은 주문 요청이 불가합니다.", Toast.LENGTH_LONG)
                                .show()
                            Log.d("WaitingRoomScreen", "자식 프로필은 주문 요청이 불가합니다.")
                        }
                        closeSTOMP(context)
                        scope.launch {
                            delay(2000)
                            navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }

        }

    }




    Scaffold(topBar = {
        DefaultAppBar(
            title = "대기방", navController = navController
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                // 🔄 로딩 중일 때 스피너 표시
                CircularProgressIndicator()
                Text(
                    text = "연결 중...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                // 로딩이 끝난 후 표시할 UI
                Text(text = "연결 완료!")
                CountdownTimer(navController)
            }
        }
    }
}

@Composable
fun CountdownTimer(navController: NavHostController) {
    var seconds by remember { mutableIntStateOf(5) }

//    LaunchedEffect(Unit) {
//        while (seconds > 0) {
//            delay(1000) // 1초 대기
//            seconds--  // 초 감소
//        }
//    }
//    LaunchedEffect(seconds) {
//        if (seconds == 0) {
//            Log.d("CountdownTimer", "타이머 종료")
//            navController.navigate(BottomRoute.HomeRoute, builder = {
//                popUpTo(BottomRoute.HomeRoute) {
//                    inclusive = true
//                }
//            })
//        }
//    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            while (seconds > 0) {
                delay(1000)
                seconds--
            }
            Log.d("CountdownTimer", "타이머 종료")
//            navController.navigate(BottomRoute.HomeRoute, builder = {
//                popUpTo(BottomRoute.HomeRoute) {
//                    inclusive = true
//                }
//            })
        }
    }

    Text(
        text = if (seconds > 0) "${seconds}초 후에 00이 됩니다." else "00",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}
