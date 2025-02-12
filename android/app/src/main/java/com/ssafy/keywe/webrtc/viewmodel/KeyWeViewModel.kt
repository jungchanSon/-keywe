package com.ssafy.keywe.webrtc.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.ssafy.keywe.BuildConfig
import com.ssafy.keywe.webrtc.RemoteControlService
import com.ssafy.keywe.webrtc.ScreenSizeManager
import com.ssafy.keywe.webrtc.data.Drag
import com.ssafy.keywe.webrtc.data.MessageData
import com.ssafy.keywe.webrtc.data.MessageType
import com.ssafy.keywe.webrtc.data.ScreenSize
import com.ssafy.keywe.webrtc.data.Touch
import com.ssafy.keywe.webrtc.ui.VideoStatsInfo
import com.ssafy.keywe.webrtc.utils.TokenUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.agora.api.example.compose.data.SettingPreferences
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.ScreenCaptureParameters
import io.agora.rtc2.video.VideoEncoderConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Inject


@HiltViewModel
class KeyWeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val screenSizeManager: ScreenSizeManager,
    application: Application,
) : AndroidViewModel(application) {
    private val _rtcEngine = MutableStateFlow<RtcEngine?>(null)
    val rtcEngine = _rtcEngine.asStateFlow()

    private val _isKiosk: Boolean = savedStateHandle["isKiosk"] ?: true

    private val _screenUid = MutableStateFlow<Int?>(null)
    val screenUid = _screenUid.asStateFlow()

    private val _localStats = MutableStateFlow<VideoStatsInfo?>(VideoStatsInfo())
    val localStats = _localStats.asStateFlow()

    private val _remoteStats = MutableStateFlow<VideoStatsInfo?>(VideoStatsInfo())
    val remoteStats = _remoteStats.asStateFlow()

    private val _screenCaptureParameters = MutableStateFlow<ScreenCaptureParameters?>(null)
    val screenCaptureParameters = _screenCaptureParameters.asStateFlow()

    private val applicationContext: Context = application.applicationContext

    private val _localDataStreamId = MutableStateFlow<Int?>(null)
    private val _remoteDataStreamId = MutableStateFlow<Int?>(null)

    private var _localScreenSize: ScreenSize? = null
    private var _remoteScreenSize: ScreenSize? = null
    val module = SerializersModule {
        polymorphic(MessageData::class) {
            subclass(Touch::class, Touch.serializer())
            subclass(Drag::class, Drag.serializer())
            subclass(ScreenSize::class, ScreenSize.serializer())
        }
    }

    val json = Json {
        serializersModule = module
        classDiscriminator = "type"  // JSON 변환 시 타입 정보 유지
    }


    init {
        // 내 기기 화면 사이즈 설정
        _localScreenSize = screenSizeManager.screenSize

        Log.d("ScreenSharing", "init ScreenSharing")
        if (!_isKiosk) _screenCaptureParameters.update { ScreenCaptureParameters() }

        _rtcEngine.update {
            RtcEngine.create(RtcEngineConfig().apply {
                mAreaCode = SettingPreferences.getArea()
                mContext = applicationContext
                mAppId = BuildConfig.AGORA_APP_ID
                mEventHandler = object : IRtcEngineEventHandler() {
                    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                        super.onJoinChannelSuccess(channel, uid, elapsed)
                        Log.d("ScreenSharing", "onJoinChannelSuccess: $uid")
//                        isJoined = true
//                        localUid = uid
//                        if (isScreenPreview) {
//                            screenUid = localUid
//                        }

                        val streamId = _rtcEngine.value?.run {
                            val streamId = createDataStream(true, true)
                            _localDataStreamId.update { streamId }
                            streamId
                        }
                        sendScreenSize(streamId, it)
                    }

                    override fun onLeaveChannel(stats: RtcStats?) {
                        super.onLeaveChannel(stats)
//                        isJoined = false
//                        localUid = 0
//                        localStats = VideoStatsInfo()
//                        remoteUid = 0
//                        remoteStats = VideoStatsInfo()
//                        screenUid = 0
                        _screenUid.update { null }
                    }

                    override fun onUserJoined(uid: Int, elapsed: Int) {
                        super.onUserJoined(uid, elapsed)
                        Log.d("ScreenSharing", "onUserJoined: $uid")
                        if (!_isKiosk) {
                            // 헬퍼 입장에서 다른 사용자가 채널에 들어 올 경우 해당 사용자의 uid 로 screenUid 업데이트
                            _screenUid.update { uid }
                        }
                        sendScreenSize(_localDataStreamId.value, _rtcEngine.value)
//                        _localDataStreamId.value?.also { id ->
//                            Log.d("sendGesture", "send local Screen Size")
//                            val jsonString = Json.encodeToString(_localScreenSize)
//                            val messageBytes = jsonString.toByteArray(Charsets.UTF_8)
//                            it?.sendStreamMessage(id, messageBytes)
//                        }
//                        sendGesture()
//                        remoteUid = uid
//                        remoteStats = VideoStatsInfo()
                    }

                    override fun onUserOffline(uid: Int, reason: Int) {
                        super.onUserOffline(uid, reason)
                        Log.d("ScreenSharing", "onUserOffline: $uid")
//                        if (remoteUid == uid) {
//                            remoteUid = 0
//                            remoteStats = VideoStatsInfo()
//                        }
                        _screenUid.update { null }
                        _remoteScreenSize = null
                    }

                    override fun onRtcStats(stats: RtcStats?) {
                        super.onRtcStats(stats)
//                        Log.d("ScreenSharing", "onRtcStats: $stats")
                        _localStats.update {
                            it?.copy(rtcStats = stats)
                        }
                    }

                    override fun onLocalVideoStats(
                        source: Constants.VideoSourceType?,
                        stats: LocalVideoStats?,
                    ) {
                        super.onLocalVideoStats(source, stats)
//                        Log.d("ScreenSharing", "onLocalVideoStats: $stats")
                        _localStats.update {
                            it?.copy(localVideoStats = stats)
                        }
                    }

                    override fun onLocalAudioStats(stats: LocalAudioStats?) {
                        super.onLocalAudioStats(stats)
//                        Log.d("ScreenSharing", "onLocalAudioStats: $stats")
                        _localStats.update {
                            it?.copy(localAudioStats = stats)
                        }
                    }

                    override fun onRemoteVideoStats(stats: RemoteVideoStats?) {
                        super.onRemoteVideoStats(stats)
                        val uid = stats?.uid ?: return
//                        Log.d("ScreenSharing", "onRemoteVideoStats: $stats")
                        if (_isKiosk && _screenUid.value == uid) {
                            _remoteStats.update {
                                it?.copy(remoteVideoStats = stats)
                            }
                        }
                    }

                    override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
                        super.onRemoteAudioStats(stats)
                        val uid = stats?.uid ?: return
//                        Log.d("ScreenSharing", "onRemoteAudioStats: $stats")
                        if (_isKiosk && _screenUid.value == uid) {
                            _remoteStats.update {
                                it?.copy(remoteAudioStats = stats)
                            }
                        }
                    }

                    override fun onLocalVideoStateChanged(
                        source: Constants.VideoSourceType?,
                        state: Int,
                        reason: Int,
                    ) {
                        super.onLocalVideoStateChanged(source, state, reason)
//                        Log.d("ScreenSharing", "onLocalVideoStateChanged: $source $state $reason")
                        if (source == Constants.VideoSourceType.VIDEO_SOURCE_SCREEN_PRIMARY) {
                            if (state == Constants.LOCAL_VIDEO_STREAM_STATE_CAPTURING) {
//                                isScreenSharing = true
                            } else if (state == Constants.LOCAL_VIDEO_STREAM_STATE_FAILED || state == Constants.LOCAL_VIDEO_STREAM_STATE_STOPPED) {
//                                isScreenSharing = false
                            }
                        }
                    }

                    override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray) {
                        super.onStreamMessage(uid, streamId, data)
                        if (_remoteDataStreamId.value == null) {
                            _remoteDataStreamId.update {
                                streamId
                            }
                        }

                        // 바이트 배열을 JSON 문자열로 변환
                        val jsonString = data.toString(Charsets.UTF_8)

                        if (_remoteScreenSize == null && jsonString.contains(MessageType.ScreenSize.name)) {
                            _remoteScreenSize = Json.decodeFromString<ScreenSize>(jsonString)
                            Log.d("sendGesture", "상대방 스크린 사이즈: $_remoteScreenSize")
                        } else if (_remoteScreenSize != null) {
                            try {
                                // JSON 문자열을 MyData 객체로 역직렬화
                                val gestureData = Json.decodeFromString<MessageData>(jsonString)
                                Log.d("sendGesture", "수신된 데이터: $uid $streamId $gestureData")

                                // 받은 데이터를 기반으로 필요한 동작 수행
                                // todo 점유 할 때만 작동
                                if (_localScreenSize != null) handleRemoteControl(gestureData)

                            } catch (e: Exception) {
                                Log.e("sendGesture", "JSON 파싱 오류: ${e.message}")
                            }
                        } else {
                            // todo 이런 경우 다시 상대방에게 재요청
                            Log.e("sendGesture", "상대방 스크린 사이즈 설정 X")
//                            sendScreenSize(_localDataStreamId.value, _rtcEngine.value)
                        }
                    }

                    override fun onStreamMessageError(
                        uid: Int,
                        streamId: Int,
                        error: Int,
                        missed: Int,
                        cached: Int,
                    ) {
                        super.onStreamMessageError(uid, streamId, error, missed, cached)
                        Log.d(
                            "ScreenSharing",
                            "onStreamMessageError: $uid $streamId $error $missed $cached"
                        )
                    }

                }
            }).apply {
                setVideoEncoderConfiguration(
                    VideoEncoderConfiguration(
                        SettingPreferences.getVideoDimensions(),
                        SettingPreferences.getVideoFrameRate(),
                        VideoEncoderConfiguration.STANDARD_BITRATE,
                        SettingPreferences.getOrientationMode()
                    )
                )
                enableVideo()
            }
        }

    }

    private fun sendScreenSize(streamId: Int?, it: RtcEngine?) {
        streamId?.also { id ->
//            val message = _localScreenSize?.toMessage()
            val jsonString = json.encodeToString(_localScreenSize)
            val messageBytes = jsonString.toByteArray(Charsets.UTF_8)
            Log.d("sendGesture", "send local Screen Size $jsonString")
            it?.sendStreamMessage(id, messageBytes)
        }
    }


    fun joinChannel(channelName: String) {
        if (_isKiosk) {
            _rtcEngine.value?.startScreenCapture(_screenCaptureParameters.value)
            val mediaOptions = ChannelMediaOptions()
            mediaOptions.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            mediaOptions.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            mediaOptions.autoSubscribeAudio = true
            mediaOptions.autoSubscribeVideo = true
            mediaOptions.publishCameraTrack = false
            mediaOptions.publishMicrophoneTrack = false
            mediaOptions.publishScreenCaptureAudio = true
            mediaOptions.publishScreenCaptureVideo = true
            TokenUtils.gen(channelName, 0) {
                _rtcEngine.value?.joinChannel(it, channelName, 0, mediaOptions)
            }
        } else {
            val mediaOptions = ChannelMediaOptions()
            mediaOptions.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            mediaOptions.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            mediaOptions.autoSubscribeAudio = true
            mediaOptions.autoSubscribeVideo = true
            mediaOptions.publishCameraTrack = false
            mediaOptions.publishMicrophoneTrack = false
            mediaOptions.publishScreenCaptureAudio = true
            mediaOptions.publishScreenCaptureVideo = true
            TokenUtils.gen(channelName, 0) {
                _rtcEngine.value?.joinChannel(it, channelName, 0, mediaOptions)
            }
        }

    }

    fun exit() {
        _rtcEngine.value?.let {
            it.stopScreenCapture()
            it.stopPreview(Constants.VideoSourceType.VIDEO_SOURCE_SCREEN_PRIMARY)
            it.leaveChannel()
        }
        RtcEngine.destroy()
    }

    fun sendClickGesture(gestureData: MessageData) {
        // JSON으로 직렬화
        val jsonString = json.encodeToString(gestureData)
        val messageBytes = jsonString.toByteArray(Charsets.UTF_8)
        Log.d(
            "sendGesture",
            "전송된 데이터: dataStreamId = ${_localDataStreamId.value}, data = $gestureData"
        )
        val result = _localDataStreamId.value?.let {
            _rtcEngine.value?.sendStreamMessage(it, messageBytes)
        }
        // todo 로컬 사이즈 크기 전송
        if (result != 0) {
            Log.e("sendGesture", "메시지 전송 실패: $result")
        } else {
            Log.d("sendGesture", "메시지 전송 성공")
        }
    }

    private fun handleRemoteControl(gestureData: MessageData) {
        // 상대방에서 터치한 offset 수신
        val convertOffset = _localScreenSize!!.convertGestureToAnotherScreen(
            _remoteScreenSize!!, Offset(gestureData.x, gestureData.y)
        )
        val intent = Intent(applicationContext, RemoteControlService::class.java)
        when (gestureData) {
            is Touch -> intent.action = MessageType.Touch.name
            is Drag -> intent.action = MessageType.Drag.name
            is ScreenSize -> MessageType.ScreenSize.name
        }

//        intent.action = gestureData.type.name

//        ScreenRatioUtil.dpToPixel(convertOffset.x)


        intent.putExtra("x", convertOffset.x)
        intent.putExtra("y", convertOffset.y)
        applicationContext.startService(intent)
    }

}