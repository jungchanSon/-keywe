package com.ssafy.keywe.webrtc.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.data.websocket.SignalService
import com.ssafy.keywe.data.websocket.SignalType
import com.ssafy.keywe.ui.theme.h4
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
fun HelperWaitingRoomScreen(
    navController: NavHostController,
    sessionId: String,
    storeId: String,
    kioskUserId: String,
    signalViewModel: SignalViewModel = hiltViewModel(),
) {
    // 초기 값은 null일 수 있으므로 안전하게 처리
    val message by signalViewModel.stompMessageFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isLoading by remember { mutableStateOf(true) }

    if (message != null) {
        Text(text = "Received message: ${message}")
    } else {
        Text(text = "No message yet")
    }
    var text = "연결 중 입니다."

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        connectSTOMP(context)
    }

    LaunchedEffect(message) {
        message?.let {
            when (it.type) {
                STOMPTYPE.REQUESTED -> {
                    // 연결 완료 후 구독
                    subscribeSTOMP(context, "677367955509677381")
                    Log.d("WaitingRoomScreen", "구독 중입니다.")
                    text = "구독 중입니다."
                }

                STOMPTYPE.SUBSCRIBE -> {
                    // 연결 완료 후 구독
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "연결 중...",
                            style = h4,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    }

//                    CountdownTimer(navController, signalViewModel)
                }
            } else {
                // 로딩이 끝난 후 표시할 UI
                Text(text = "연결 완료!")
//                CountdownTimer(navController, signalViewModel)
            }
        }
    }
}

//@Composable
//fun CountdownTimer(navController: NavHostController) {
//    var seconds by remember { mutableIntStateOf(10) }
//    var isConnected by remember { mutableStateOf(false) }
//
////    LaunchedEffect(Unit) {
////        while (seconds > 0) {
////            delay(1000) // 1초 대기
////            seconds--  // 초 감소
////        }
////    }
////    LaunchedEffect(seconds) {
////        if (seconds == 0) {
////            Log.d("CountdownTimer", "타이머 종료")
////            navController.navigate(BottomRoute.HomeRoute, builder = {
////                popUpTo(BottomRoute.HomeRoute) {
////                    inclusive = true
////                }
////            })
////        }
////    }
//
////    val scope = rememberCoroutineScope()
//
////    LaunchedEffect(Unit) {
////        scope.launch {
////            while (seconds > 0) {
////                delay(1000)
////                seconds--
////            }
////            Log.d("CountdownTimer", "타이머 종료")
//////            navController.navigate(BottomRoute.HomeRoute, builder = {
//////                popUpTo(BottomRoute.HomeRoute) {
//////                    inclusive = true
//////                }
//////            })
////        }
////    }
//    var dotCount by remember { mutableStateOf(1) }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(500) // 0.5초마다 변경
//            dotCount = (dotCount % 3) + 1 // 1~3 순환
//        }
//    }
//
//    LaunchedEffect(seconds) {
//        if (seconds > 0) {
//            delay(1000) // 1초 대기
//            seconds--  // 초 감소
//        } else {
//            Log.d("CountdownTimer", "타이머 종료")
////            navController.navigate(Route.MenuBaseRoute.ConnectingScreenRoute) {
////                popUpTo(Route.MenuBaseRoute.ConnectingScreenRoute) {
////                    inclusive = true
////                }
////            }
//        }
//    }
//
//    if (isConnected) {
//        Column(modifier = Modifier
////            .padding(innerpadding)
//            .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center)
//        {
//            Image(
//                painter = painterResource(id = R.drawable.humanimage),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(width = 190.dp, height = 231.dp)
//            )
//
//            Box(modifier = Modifier.width(300.dp).height(153.dp), contentAlignment = Alignment.Center){
//                Text("김싸피님과\n연결되었습니다.", style= h4, textAlign = TextAlign.Center)
//            }
//        }
//    } else if (!isConnected && seconds > 0) {
//        Text(
//            text = if (seconds > 0) "${seconds}초 후에 연결이 됩니다." else "00",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}

//@Composable
//fun CountdownTimer(navController: NavHostController, signalViewModel: SignalViewModel) {
//
//    val seconds by signalViewModel.seconds.collectAsStateWithLifecycle()
//    val isConnected by signalViewModel.isConnected.collectAsStateWithLifecycle()
//
//    var dotCount by remember { mutableStateOf(1) }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(500) // 0.5초마다 변경
//            dotCount = (dotCount % 3) + 1 // 1~3 순환
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        when {
//            isConnected -> {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.humanimage),
//                        contentDescription = null,
//                        modifier = Modifier.size(width = 190.dp, height = 231.dp)
//                    )
//                    Box(
//                        modifier = Modifier.width(300.dp).height(153.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text("김싸피님과\n연결되었습니다.", style = h4, textAlign = TextAlign.Center)
//                    }
//                }
//            }
//
//            seconds > 0 -> {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(20.dp)
//                    ) {
//                        Text(
//                            text = "연결 중" + ".".repeat(dotCount),
//                            style = h4,
//                            modifier = Modifier.padding(top = 16.dp)
//                        )
//                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
//                    }
//                    Text(
//                        text = "남은 시간: ${seconds}초",
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//
//            else -> {
//                Text(
//                    text = "연결 시간이 초과 되었습니다.",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
//
////    LaunchedEffect(Unit) {
////        while (seconds > 0) {
////            delay(1000) // 1초 대기
////            seconds--  // 초 감소
////        }
////    }
////    LaunchedEffect(seconds) {
////        if (seconds == 0) {
////            Log.d("CountdownTimer", "타이머 종료")
////            navController.navigate(BottomRoute.HomeRoute, builder = {
////                popUpTo(BottomRoute.HomeRoute) {
////                    inclusive = true
////                }
////            })
////        }
////    }
//
////    val scope = rememberCoroutineScope()
//
////    LaunchedEffect(Unit) {
////        scope.launch {
////            while (seconds > 0) {
////                delay(1000)
////                seconds--
////            }
////            Log.d("CountdownTimer", "타이머 종료")
//////            navController.navigate(BottomRoute.HomeRoute, builder = {
//////                popUpTo(BottomRoute.HomeRoute) {
//////                    inclusive = true
//////                }
//////            })
////        }
////    }
////    LaunchedEffect(seconds) {
////        if (seconds > 0) {
////            delay(1000) // 1초 대기
////            seconds--  // 초 감소
////        } else {
////            Log.d("CountdownTimer", "타이머 종료")
//////            navController.navigate(Route.MenuBaseRoute.ConnectingScreenRoute) {
//////                popUpTo(Route.MenuBaseRoute.ConnectingScreenRoute) {
//////                    inclusive = true
//////                }
//////            }
////        }
////    }
////
////    if (isConnected) {
////        Column(modifier = Modifier
//////            .padding(innerpadding)
////            .fillMaxSize(),
////            horizontalAlignment = Alignment.CenterHorizontally,
////            verticalArrangement = Arrangement.Center)
////        {
////            Image(
////                painter = painterResource(id = R.drawable.humanimage),
////                contentDescription = null,
////                modifier = Modifier
////                    .size(width = 190.dp, height = 231.dp)
////            )
////
////            Box(modifier = Modifier.width(300.dp).height(153.dp), contentAlignment = Alignment.Center){
////                Text("김싸피님과\n연결되었습니다.", style= h4, textAlign = TextAlign.Center)
////            }
////        }
////    } else if (!isConnected && seconds > 0) {
////        Text(
////            text = if (seconds > 0) "${seconds}초 후에 연결이 됩니다." else "00",
////            fontSize = 24.sp,
////            fontWeight = FontWeight.Bold,
////            modifier = Modifier.padding(16.dp)
////        )
////    } else {
////        Text(
////            text = "연결 시간이 초과 되었습니다.",
////            fontSize = 24.sp,
////            fontWeight = FontWeight.Bold,
////            modifier = Modifier.padding(16.dp)
////        )
////    }
//}
