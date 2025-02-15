package com.ssafy.keywe.webrtc.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.websocket.SignalService
import com.ssafy.keywe.data.websocket.SignalType
import com.ssafy.keywe.presentation.kiosk.component.NoTitleOneButtonDialog
import com.ssafy.keywe.ui.theme.h4
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel
import kotlinx.coroutines.delay

fun connectSTOMP(context: Context, token: String) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.CONNECT.name
    intent.putExtra("token", token)
    context.startService(intent)
}

public fun subscribeSTOMP(context: Context, profileId: String) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.SUBSCRIBE.name
    intent.putExtra(
        "profileId", profileId
    )
    context.startService(intent)
}

private fun requestSTOMP(context: Context, storeId: String) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.REQUEST.name
    intent.putExtra(
        "storeId", storeId
    )
    context.startService(intent)
}

fun closeSTOMP(context: Context) {
    val intent = Intent(context, SignalService::class.java)
    intent.action = SignalType.CLOSE.name
    context.startService(intent)
}


@Composable
fun ParentWaitingRoomScreen(
    navController: NavHostController,
    signalViewModel: SignalViewModel = hiltViewModel(),
    keyWeViewModel: KeyWeViewModel = hiltViewModel(),
    tokenManager: TokenManager,
) {
    // 초기 값은 null일 수 있으므로 안전하게 처리
    val message by signalViewModel.stompMessageFlow.collectAsStateWithLifecycle()
    val connected by signalViewModel.connected.collectAsStateWithLifecycle()
    val subscribed by signalViewModel.subscribed.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val profileId by ProfileIdManager.profileId.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val rtcConnected by keyWeViewModel.connected.collectAsStateWithLifecycle()


    var isConnected by remember { mutableStateOf(false) }
    var dotCount by remember { mutableStateOf(1) }
    var isTimeOut by remember { mutableStateOf(false) }
    var errorMessageInConnecting by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Log.d("WaitingRoomScreen", "LaunchedEffect Connect")
        val token = tokenManager.getToken()!!
        connectSTOMP(context, token)
    }

    LaunchedEffect(connected) {
        if (connected) {
            // 연결 후 구독
            subscribeSTOMP(context, profileId.toString())
        }
    }

    LaunchedEffect(subscribed) {
        if (subscribed) {
            //구독 후 요청
            val storeId = tokenManager.getStoreId()
            Log.d("Request", "request = $storeId")

            // todo 사장 로그인으로 가져온 storeId
            requestSTOMP(context, storeId.toString())
        }
    }

//    DisposableEffect() { }

    LaunchedEffect(message) {
        if (profileId != null) {
            message?.let {
                when (it.type) {
                    STOMPTYPE.SUBSCRIBE -> {
                        // 연결 완료 후 구독
                    }

                    STOMPTYPE.REQUESTED -> {
                        Log.d(
                            "WaitingRoomScreen",
                            "요청 ${it.data!!.success}개 성공 ${it.data!!.failure}개 실패."
                        )
                    }

                    STOMPTYPE.WAITING -> {
                        Log.d(
                            "WaitingRoomScreen",
                            "알림 ${it.data!!.success}개 성공 ${it.data!!.failure}개 실패."
                        )
                    }

                    STOMPTYPE.ACCEPTED -> {
                        val sessionId = it.data!!.sessionId!!
                        val helperUserId = it.data.helperUserId
                        val kioskUserId = it.data.kioskUserId
                        val channel = it.data.channel!!
                        isConnected = true
                        keyWeViewModel.connectWebRTC()
                        keyWeViewModel.joinChannel(channel)
                        keyWeViewModel.remoteStats.collect { state ->
                            if (state != null) {
                                Log.d("WaitingRoomScreen", "connect remote")
                                navController.navigate(Route.MenuBaseRoute.MenuRoute) {
                                    popUpTo(Route.MenuBaseRoute.KioskHomeRoute)
                                    launchSingleTop = true
                                }
                            } else {
                                Log.d("WaitingRoomScreen", "state is null")
                            }
                        }

                    }

                    STOMPTYPE.TIMEOUT -> {
                        Log.d("WaitingRoomScreen", "타임아웃")
                        isTimeOut = true
                        closeSTOMP(context)
                    }

                    STOMPTYPE.END -> {
                        Log.d("WaitingRoomScreen", "종료")
                        closeSTOMP(context)
                        keyWeViewModel.exit()
                    }

                    STOMPTYPE.ERROR -> {
                        if (it.data!!.code == "R100") {
                            // 만료된 주문 요청입니다
                            errorMessageInConnecting = "만료된 주문 요청입니다."
                            Log.d("WaitingRoomScreen", "만료된 주문 요청입니다.")

                        } else if (it.data.code == "R101") {
                            // 가족에게 요청을 보내지 못했어요.
                            errorMessageInConnecting = "가족에게 요청을 보내지 못했어요."
                            Log.d("WaitingRoomScreen", "가족에게 요청을 보내지 못했어요.")
                        } else {
                            // 자식 프로필은 주문 요청이 불가합니다.
                            errorMessageInConnecting = "자식 프로필은 주문 요청이 불가합니다."
                            Log.d("WaitingRoomScreen", "자식 프로필은 주문 요청이 불가합니다.")
                        }
                        closeSTOMP(context)
//                        scope.launch {
//                            delay(2000)
//                            navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
//                                popUpTo(navController.graph.startDestinationId)
//                                launchSingleTop = true
//                            }
//                        }
                    }
                }
            }

        }

    }


    // 점 개수 변경
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dotCount = (dotCount % 3) + 1
        }
    }

    Scaffold(topBar = {
        DefaultAppBar(
            title = "대기방", navController = navController
        )
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isConnected -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.humanimage),
                            contentDescription = null,
                            modifier = Modifier.size(width = 190.dp, height = 231.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .height(153.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("김싸피님과\n연결되었습니다.", style = h4, textAlign = TextAlign.Center)
                        }
                    }
                }

                !isConnected -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "연결 중" + ".".repeat(dotCount),
                            style = h4,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
//            Button(
//                onClick = { isConnected = !isConnected }, modifier = Modifier.padding(top = 20.dp)
//            ) {
//                Text(
//                    text = if (isConnected) "연결 해제" else "연결하기", fontSize = 18.sp
//                )
//            }
        }
    }

    if (isTimeOut) {
        NoTitleOneButtonDialog(description = "연결 시간이 초과되었습니다.", onConfirm = {
            isTimeOut = false
            navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
                restoreState = true
            }
        })
    }

    if (errorMessageInConnecting != null) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .background(
                    color = if (errorMessageInConnecting != null) titleTextColor.copy(
                        alpha = 0.5f
                    ) else Color.Transparent
                )
        ) {
            // 개별 삭제 다이얼로그
            if (errorMessageInConnecting != null) {
                NoTitleOneButtonDialog(description = errorMessageInConnecting!!, onConfirm = {
                    errorMessageInConnecting = null
                    navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    }
}