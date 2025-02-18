package com.ssafy.keywe.webrtc.viewmodel

//import com.ssafy.keywe.webrtc.data.Drag
//import com.ssafy.keywe.webrtc.data.ScreenSize
//import com.ssafy.keywe.webrtc.data.Touch
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.ssafy.keywe.BuildConfig
import com.ssafy.keywe.webrtc.RemoteControlService
import com.ssafy.keywe.webrtc.ScreenSizeManager
import com.ssafy.keywe.webrtc.data.ChannelData
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.data.MessageData
import com.ssafy.keywe.webrtc.data.MessageType
import com.ssafy.keywe.webrtc.ui.AudioStatsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.agora.api.example.compose.data.SettingPreferences
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
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

    private val _localStats = MutableStateFlow<AudioStatsInfo?>(null)
    val localStats = _localStats.asStateFlow()

    private val _remoteStats = MutableStateFlow<AudioStatsInfo?>(null)
    val remoteStats = _remoteStats.asStateFlow()

    private val _audioRoute = MutableStateFlow<Int?>(Constants.AUDIO_ROUTE_SPEAKERPHONE)
    val audioRoute = _audioRoute.asStateFlow()

    private var _systemUiHeight = screenSizeManager.statusBarHeightPx

    private val applicationContext: Context = application.applicationContext

    private val _localDataStreamId = MutableStateFlow<Int?>(null)
    private val _remoteDataStreamId = MutableStateFlow<Int?>(null)

//    private var _localScreenSize: ScreenSize? = null
//    private var _remoteScreenSize: ScreenSize? = null

    private val _connected = MutableStateFlow<Boolean>(true)
    val connected = _connected.asStateFlow()

    init {
        Log.d("KeyWeViewModel", "init")
    }

    override fun onCleared() {
        Log.d("KeyWeViewModel", "onCleared")
        super.onCleared()
    }

    val module = SerializersModule {
        polymorphic(MessageData::class) {
            subclass(
                KeyWeButtonEvent.CategorySelect::class, KeyWeButtonEvent.CategorySelect.serializer()
            )
            subclass(KeyWeButtonEvent.MenuSelect::class, KeyWeButtonEvent.MenuSelect.serializer())
            subclass(
                KeyWeButtonEvent.MenuAddToCart::class, KeyWeButtonEvent.MenuAddToCart.serializer()
            )
        }
    }

    val json = Json {
        serializersModule = module
        classDiscriminator = "type"  // JSON 변환 시 타입 정보 유지
    }

    fun connectWebRTC() {
        // 내 기기 화면 사이즈 설정
        _connected.update { true }
//        _localScreenSize = screenSizeManager.screenSize
        Log.d("KeyWeViewModel", "init connectWebRTC")

        _rtcEngine.update {
            RtcEngine.create(RtcEngineConfig().apply {
                mAreaCode = SettingPreferences.getArea()
                mContext = applicationContext
                mAppId = BuildConfig.AGORA_APP_ID
                mEventHandler = object : IRtcEngineEventHandler() {

                    override fun onAudioRouteChanged(routing: Int) {
                        _audioRoute.update {
                            routing
                        }
                        super.onAudioRouteChanged(routing)
                    }


                    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                        super.onJoinChannelSuccess(channel, uid, elapsed)
                        Log.d("ScreenSharing", "onJoinChannelSuccess: $uid")

                        val streamId = _rtcEngine.value?.run {
                            val streamId = createDataStream(true, true)
                            _localDataStreamId.update { streamId }
                            streamId
                        }
//                        sendScreenSize(streamId, it)
                        _localStats.update {
                            AudioStatsInfo()
                        }
                    }

                    override fun onLeaveChannel(stats: RtcStats?) {
                        super.onLeaveChannel(stats)
                        _localStats.update { null }
                        _remoteStats.update { null }
                    }

                    override fun onUserJoined(uid: Int, elapsed: Int) {
                        super.onUserJoined(uid, elapsed)
                        Log.d("ScreenSharing", "onUserJoined: $uid")
//                        sendScreenSize(_localDataStreamId.value, _rtcEngine.value)
                        _remoteStats.update {
                            AudioStatsInfo()
                        }
                    }

                    override fun onUserOffline(uid: Int, reason: Int) {
                        super.onUserOffline(uid, reason)
                        Log.d("ScreenSharing", "onUserOffline: $uid")
//                        _remoteScreenSize = null
                        _remoteStats.update { null }
                    }

                    override fun onLocalAudioStats(stats: LocalAudioStats?) {
                        super.onLocalAudioStats(stats)
//                        Log.d("ScreenSharing", "onLocalAudioStats: $stats")
                        _localStats.update {
                            it?.copy(localAudioStats = stats)
                        }
                    }

                    override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
                        super.onRemoteAudioStats(stats)
                        val uid = stats?.uid ?: return
//                        Log.d("ScreenSharing", "onRemoteAudioStats: $stats")
                        _remoteStats.update {
                            it?.copy(remoteAudioStats = stats)
                        }

                    }

                    override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray) {
                        super.onStreamMessage(uid, streamId, data)
                        if (_remoteDataStreamId.value == null) {
                            _remoteDataStreamId.update {
                                streamId
                            }
                        }

                        try {
                            val jsonString = data.toString(Charsets.UTF_8)
                            val buttonEvent = Json.decodeFromString<MessageData>(jsonString)
                            Log.d("KeyWeViewModel", "이벤트 수신: $buttonEvent")
                            handleRemoteControl(buttonEvent)
                        } catch (e: Exception) {
                            Log.e("KeyWeViewModel", "메시지 처리 오류", e)
                        }
                    }

//                    override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray) {
//                        super.onStreamMessage(uid, streamId, data)
//                        if (_remoteDataStreamId.value == null) {
//                            _remoteDataStreamId.update {
//                                streamId
//                            }
//                        }
//
//                        // 바이트 배열을 JSON 문자열로 변환
//                        val jsonString = data.toString(Charsets.UTF_8)
//
////                        if (_remoteScreenSize == null && jsonString.contains(MessageType.ScreenSize.name)) {
////                            _remoteScreenSize = Json.decodeFromString<ScreenSize>(jsonString)
////                            Log.d("sendGesture", "상대방 스크린 사이즈: $_remoteScreenSize")
////                        } else if (_remoteScreenSize != null) {
////                            try {
////                                // JSON 문자열을 MyData 객체로 역직렬화
////                                val gestureData = Json.decodeFromString<MessageData>(jsonString)
////                                Log.d(
////                                    "sendGesture",
////                                    "=======================메시지 수신 성공======================="
////                                )
////
////                                Log.d("sendGesture", "수신된 데이터: $uid $streamId $gestureData")
////
////                                // 받은 데이터를 기반으로 필요한 동작 수행
////                                // todo 점유 할 때만 작동
////                                if (_localScreenSize != null) handleRemoteControl(gestureData)
////
////
////                            } catch (e: Exception) {
////                                Log.e("sendGesture", "JSON 파싱 오류: ${e.message}")
////                            }
////                        } else {
////                            // todo 이런 경우 다시 상대방에게 재요청
////                            Log.e("sendGesture", "상대방 스크린 사이즈 설정 X")
//////                            sendScreenSize(_localDataStreamId.value, _rtcEngine.value)
////                        }
//                    }

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
                enableAudio()
                setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
            }
        }

    }

    fun joinChannel(channelData: ChannelData) {
        val mediaOptions = ChannelMediaOptions()
        mediaOptions.publishCameraTrack = false
        mediaOptions.publishMicrophoneTrack = true
        mediaOptions.autoSubscribeAudio = true
        mediaOptions.autoSubscribeVideo = false
        _rtcEngine.value?.joinChannel(channelData.token, channelData.name, 0, mediaOptions)
    }

//    fun sendClickGesture(gestureData: MessageData) {
//        // JSON으로 직렬화
//        val jsonString = json.encodeToString(gestureData)
//        val messageBytes = jsonString.toByteArray(Charsets.UTF_8)
//        Log.d(
//            "sendGesture",
//            "전송된 데이터: dataStreamId = ${_localDataStreamId.value}, data = $gestureData"
//        )
//        val result = _localDataStreamId.value?.let {
//            _rtcEngine.value?.sendStreamMessage(it, messageBytes)
//        }
//        // todo 로컬 사이즈 크기 전송
//        if (result != 0) {
//            Log.e("sendGesture", "메시지 전송 실패: $result")
//        } else {
//            Log.d("sendGesture", "=======================메시지 전송 성공=======================")
//        }
//    }

    fun toggleAudio() {
        if (_audioRoute.value == Constants.AUDIO_ROUTE_SPEAKERPHONE) {
            _rtcEngine.value?.setEnableSpeakerphone(false)
            val ret = _rtcEngine.value?.setRouteInCommunicationMode(Constants.AUDIO_ROUTE_HEADSET)
            if (ret != Constants.ERR_OK) {
//                _rtcEngine.value?.setEnableSpeakerphone(false)
                _audioRoute.update { Constants.AUDIO_ROUTE_HEADSET }
            }
        } else {
            val ret =
                _rtcEngine.value?.setRouteInCommunicationMode(Constants.AUDIO_ROUTE_SPEAKERPHONE)
            if (ret != Constants.ERR_OK) {
                _rtcEngine.value?.setEnableSpeakerphone(true)
                _audioRoute.update { Constants.AUDIO_ROUTE_SPEAKERPHONE }
            }
        }
    }

    fun exit() {
        Log.d("KeyWeViewModel", "exit")
        _rtcEngine.value?.leaveChannel()
        RtcEngine.destroy()
        _rtcEngine.value = null
        _connected.update { false }
    }

//    private fun sendScreenSize(streamId: Int?, it: RtcEngine?) {
//        streamId?.also { id ->
////            val message = _localScreenSize?.toMessage()
//            val localMetrics = screenSizeManager.screenMetrics
//
//            val size = ScreenSize(
//                MessageType.ScreenSize,
//                localMetrics.width.toFloat(),
//                localMetrics.height.toFloat(),
//                localMetrics.density
//            )
//            val jsonString = json.encodeToString(size)
//            val messageBytes = jsonString.toByteArray(Charsets.UTF_8)
//            Log.d("sendGesture", "send local Screen Size $jsonString")
//            it?.sendStreamMessage(id, messageBytes)
//        }
//    }


    //    private fun handleRemoteControl(gestureData: MessageData) {
//        val screenSizeData = gestureData as? ScreenSize
//        if (screenSizeData != null) {
//            // screenSizeData를 사용하여 ScreenSize 관련 데이터를 처리할 수 있습니다.
//            Log.d("sendGesture", "상대방 스크린 사이즈: $screenSizeData")
//        } else {
//            // gestureData가 ScreenSize가 아닌 경우에 대한 처리를 합니다.
//            Log.d("sendGesture", "gesture")
//        }
//
//
//        // 상대방에서 터치한 offset 수신
//        val convertOffset = _localScreenSize!!.convertGestureToAnotherScreen(
//            _remoteScreenSize!!, Offset(gestureData.x, gestureData.y)
//        )
//        val intent = Intent(applicationContext, RemoteControlService::class.java)
//        when (gestureData) {
//            is Touch -> intent.action = MessageType.Touch.name
//            is Drag -> intent.action = MessageType.Drag.name
//            is ScreenSize -> MessageType.ScreenSize.name
//        }
//
//        val localMetrics = screenSizeManager.screenMetrics
//        val remoteMetrics = TouchCoordinateConverter.ScreenMetrics(
//            _remoteScreenSize!!.x,
//            _remoteScreenSize!!.y,
//            _remoteScreenSize!!.density,
//            _remoteScreenSize!!.aspectRatio,
//        )
//
//        val offset = TouchCoordinateConverter.convertCoordinates(
//            gestureData.x, gestureData.y, remoteMetrics, localMetrics
//        )
//
////        intent.action = gestureData.type.name
//
////        ScreenRatioUtil.dpToPixel(convertOffset.x)
//
////        intent.putExtra("x", gestureData.x)
////        intent.putExtra("y", gestureData.y)
//        intent.putExtra("x", offset.first)
//        intent.putExtra("y", offset.second + screenSizeManager.statusBarHeightPx)
//
////        intent.putExtra("x", offset.first)
////        intent.putExtra("y", offset.second)
//        applicationContext.startService(intent)
//    }
    fun sendButtonEvent(event: KeyWeButtonEvent) {
        val jsonString = json.encodeToString<MessageData>(event)
        val messageBytes = jsonString.toByteArray(Charsets.UTF_8)
        Log.d("KeyWeViewModel", "이벤트 전송: $event")

        Log.d("KeyWeViewModel", "_localDataStreamId.value: ${_localDataStreamId.value}")
        Log.d("KeyWeViewModel", "_rtcEngine.value: ${_rtcEngine.value}")

        val result = _localDataStreamId.value?.let {
            _rtcEngine.value?.sendStreamMessage(it, messageBytes)
        }

        if (result != 0) {
            Log.e("KeyWeViewModel", "이벤트 전송 실패")
        }
    }

    private fun handleRemoteControl(messageData: MessageData) {
        Log.d("KeyWeViewModel", "handleRemoteControl 호출됨: $messageData")

        when (messageData) {
            is KeyWeButtonEvent -> {
                val intent = Intent(applicationContext, RemoteControlService::class.java).apply {
                    action = MessageType.BUTTON_EVENT.name
                    when (messageData) {
                        is KeyWeButtonEvent.CategorySelect -> {
                            putExtra("eventType", "CategorySelect")
                            putExtra("categoryId", messageData.categoryId)
                            putExtra("categoryName", messageData.categoryName)
                        }

                        is KeyWeButtonEvent.MenuSelect -> {
                            putExtra("eventType", "MenuSelect")
                            putExtra("menuId", messageData.menuId)
                            putExtra("storeId", messageData.storeId)
                        }

                        is KeyWeButtonEvent.MenuAddToCart -> {
                            putExtra("eventType", "MenuAddToCart")
                            putExtra("menuId", messageData.menuId)
                        }

                        is KeyWeButtonEvent.MenuDetailSelectCommonOption -> {
                            putExtra("eventType", "MenuDetailSelectCommonOption")
                            putExtra("optionValue", messageData.optionValue)
                        }

                        is KeyWeButtonEvent.MenuDetailPlusExtraOption -> {
                            putExtra("eventType", "MenuDetailPlusExtraOption")
                            putExtra("optionName", messageData.optionName)
                        }

                        is KeyWeButtonEvent.MenuDetailMinusExtraOption -> {
                            putExtra("eventType", "MenuDetailMinusExtraOption")
                            putExtra("optionName", messageData.optionName)
                        }

                        is KeyWeButtonEvent.MenuCartPlusAmount -> {
                            putExtra("eventType", "MenuCartPlusAmount")
                            putExtra("cartItemName", messageData.cartItemName)
                        }

                        is KeyWeButtonEvent.MenuCartMinusAmount -> {
                            putExtra("eventType", "MenuCartMinusAmount")
                            putExtra("cartItemName", messageData.cartItemName)
                        }

                        is KeyWeButtonEvent.MenuCart -> {
                            putExtra("eventType", "MenuCart")
                        }

                        is KeyWeButtonEvent.CartAcceptDialog -> {
                            putExtra(
                                "eventType", "CartAcceptDialog"
                            )
                        }

                        is KeyWeButtonEvent.CartCloseDialog -> {
                            putExtra(
                                "eventType", "CartCloseDialog"
                            )
                        }

                        is KeyWeButtonEvent.CartOpenDialog -> {
                            putExtra(
                                "eventType", "CartOpenDialog"
                            )
                        }

                        is KeyWeButtonEvent.CartIdOpenDialog -> {
                            putExtra("eventType", "CartIdOpenDialog")
                            putExtra("cartId", messageData.cartId)
                        }

                        is KeyWeButtonEvent.CartIdOpenBottomSheet -> {
                            putExtra("eventType", "CartIdOpenBottomSheet")
                            putExtra("cartId", messageData.cartId)
                        }

                        is KeyWeButtonEvent.CartAcceptBottomSheet -> {
                            putExtra("eventType", "CartAcceptBottomSheet")
                        }

                        is KeyWeButtonEvent.CartCloseBottomSheet -> {
                            putExtra("eventType", "CartCloseBottomSheet")
                        }

                        KeyWeButtonEvent.BackButton -> {
                            putExtra("eventType", "BackButton")
                        }

                        KeyWeButtonEvent.OrderButton -> {
                            putExtra("eventType", "OrderButton")
                        }

                        KeyWeButtonEvent.StoreButton -> {
                            putExtra("eventType", "StoreButton")
                        }
                    }
                }

                Log.d("KeyWeViewModel", "서비스 시작 시도: $intent")
                val result = applicationContext.startService(intent)
                Log.d("KeyWeViewModel", "서비스 시작 결과: $result")
            }
        }
    }

}