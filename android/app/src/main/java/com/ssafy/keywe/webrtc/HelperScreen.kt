package com.ssafy.keywe.webrtc

import android.content.Context
import android.util.Log
import android.view.TextureView
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.webrtc.ui.VideoStatsInfo
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import io.agora.rtc2.Constants
import io.agora.rtc2.video.VideoCanvas

@Composable
fun HelperScreen(
    channelName: String,
    navController: NavHostController,
    viewModel: KeyWeViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.joinChannel(channelName)
    }

    val rtcEngine by viewModel.rtcEngine.collectAsStateWithLifecycle()
    val statsInfo by viewModel.remoteStats.collectAsStateWithLifecycle()
    val screenUid by viewModel.screenUid.collectAsStateWithLifecycle()


    Scaffold { innerPadding ->
        Column {
            if (screenUid != null)
                VideoCell(
                    modifier = Modifier.fillMaxSize(),
                    id = screenUid ?: 0,
                    isLocal = false,
                    setupVideo = { view, id, _ ->
                        rtcEngine?.setupRemoteVideo(
                            VideoCanvas(
                                view, Constants.RENDER_MODE_HIDDEN, id
                            )
                        )
                    },
                    statsInfo = statsInfo,
                )
            else

                Text("연결 중입니다.")

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
