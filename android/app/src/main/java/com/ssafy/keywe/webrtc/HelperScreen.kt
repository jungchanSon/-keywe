package com.ssafy.keywe.webrtc

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.webrtc.data.Drag
import com.ssafy.keywe.webrtc.data.MessageType
import com.ssafy.keywe.webrtc.data.Touch
import com.ssafy.keywe.webrtc.ui.VideoStatsInfo
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel


private fun isAccessibilityServiceEnabled(
    context: Context,
    service: Class<out AccessibilityService>,
): Boolean {
    val expectedComponentName = ComponentName(context, service)
    val enabledServices = Settings.Secure.getString(
        context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    val accessibilityEnabled =
        Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)

    if (accessibilityEnabled == 1) {
        enabledServices?.split(":")?.forEach { componentName ->
            if (ComponentName.unflattenFromString(componentName) == expectedComponentName) {
                return true
            }
        }
    }
    return false
}

fun openAccessibilitySettings(context: Context) {
    if (!isAccessibilityServiceEnabled(context, RemoteControlService::class.java)) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    } else {
        Log.d("MainActivity", "Accessibility Service already enabled")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HelperScreen(
    channelName: String,
    navController: NavHostController, sendBackCommand: () -> Unit = {},
    viewModel: KeyWeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        // todo 채널 입장 전 permission 요청


//        viewModel.joinChannel(channelName)
    }

    val rtcEngine by viewModel.rtcEngine.collectAsStateWithLifecycle()
    val statsInfo by viewModel.remoteStats.collectAsStateWithLifecycle()
//    val screenUid by viewModel.screenUid.collectAsStateWithLifecycle()
//
//    LaunchedEffect(screenUid) {
//        val intent = Intent(context, RemoteControlService::class.java)
//        intent.action = "touch"
//        intent.putExtra("x", 200)
//        intent.putExtra("x", 300)
//        context.startService(intent)
////        if (screenUid != null)
////        openAccessibilitySettings(context)
//    }
    LaunchedEffect(Unit) {
        openAccessibilitySettings(context)
    }


//    BackHandler {
//        // 여기서 로컬에서는 아무 네비게이션 동작을 하지 않고,
//        // 원격 기기에 뒤로가기 명령을 전송합니다.
//        sendBackCommand()
//    }

    val density = LocalDensity.current

    Scaffold(modifier = Modifier.pointerInteropFilter { motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
//                val x = ScreenRatioUtil.pixelToDp(motionEvent.x, density)
//                val y = ScreenRatioUtil.pixelToDp(motionEvent.y, density)

                Log.d("sendGesture", "실제 클릭한 위치 x PX = ${motionEvent.x} y DP = ${motionEvent.y}")
//                Log.d("sendGesture", "실제 클릭한 위치 x DP = ${x} y DP = ${y}")
                viewModel.sendClickGesture(
                    Touch(
                        MessageType.Touch, motionEvent.x, motionEvent.y,
                    )
                )
                println("Tapped at x=${motionEvent.x}, y=${motionEvent.y}")
            }

            MotionEvent.ACTION_MOVE -> {
                viewModel.sendClickGesture(
                    Drag(
                        MessageType.Drag, motionEvent.x, motionEvent.y,
                    )
                )
                println("Moved at x=${motionEvent.x}, y=${motionEvent.y}")
            }

            else -> {
                Log.d("MotionEvent", "click")
            }
        }
        false
    }) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .clickable { }) {
//            if (screenUid != null) BottomButton(content = "데이터 전송", onClick = {
////                    viewModel.sendGesture()
//            })
//                VideoCell(
//                    modifier = Modifier.fillMaxSize(),
//                    id = screenUid ?: 0,
//                    isLocal = false,
//                    setupVideo = { view, id, _ ->
//                        rtcEngine?.setupRemoteVideo(
//                            VideoCanvas(
//                                view, Constants.RENDER_MODE_HIDDEN, id
//                            )
//                        )
//                    },
//                    statsInfo = statsInfo,
//                )
//            else

            Text("연결 중입니다.")
            BottomButton(content = "접근성 확인", onClick = {
//                    viewModel.sendGesture()
            })
            BottomButton(content = "나가기", onClick = {
                viewModel.exit()
                navController.popBackStack()
            })
        }

    }


}


@Composable
fun VideoCell(
    modifier: Modifier = Modifier,
    id: Int,
    isLocal: Boolean,
    createView: ((context: Context) -> View)? = null,
    setupVideo: (renderView: View, id: Int, isFirstSetup: Boolean) -> Unit,
    statsInfo: VideoStatsInfo? = null,
    overlay: @Composable BoxScope.() -> Unit? = { },
) {
    Box(modifier) {
        if (id != 0) {
            AndroidView(factory = { context ->
                Log.d("VideoCell", "VideoCell: create render view.")
                createView?.invoke(context) ?: TextureView(context).apply {
                    tag = id
                    setupVideo(this, id, true)
                }
            }, update = { view ->
                if (view.tag != id) {
                    Log.d("VideoCell", "VideoCell: update render view.")
                    view.tag = id
                    setupVideo(view, id, false)
                }
            })
        }

        if (statsInfo != null) {
            var text = ""
            if (isLocal) {
                statsInfo.localVideoStats?.let { text += "${it.encodedFrameWidth}x${it.encodedFrameHeight},${it.encoderOutputFrameRate}fps" }

                statsInfo.rtcStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "LM Delay: ${it.lastmileDelay}ms"
                }

                statsInfo.localVideoStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "VSend: ${it.sentBitrate}kbps"
                }

                statsInfo.localAudioStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "ASend: ${it.sentBitrate}kbps"
                }

                statsInfo.rtcStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "CPU: ${it.cpuAppUsage}%/${it.cpuTotalUsage}%"
                }

                statsInfo.rtcStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "VSend Loss: ${it.txPacketLossRate}%"
                }
            } else {
                statsInfo.remoteVideoStats?.let { text += "${it.width}x${it.height},${it.decoderOutputFrameRate}fps" }

                statsInfo.remoteVideoStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "VRecv: ${it.receivedBitrate}kbps"
                }

                statsInfo.remoteAudioStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "ARecv: ${it.receivedBitrate}kbps"
                }

                statsInfo.remoteVideoStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "VRecv Loss: ${it.packetLossRate}%"
                }

                statsInfo.remoteAudioStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "ARecv Loss: ${it.audioLossRate}%"
                }

                statsInfo.remoteAudioStats?.let {
                    if (text.isNotEmpty()) {
                        text += "\n"
                    }
                    text += "AQuality: ${it.quality}"
                }
            }
            Text(text = text, color = Color.White)
        }

        overlay()
    }
}
