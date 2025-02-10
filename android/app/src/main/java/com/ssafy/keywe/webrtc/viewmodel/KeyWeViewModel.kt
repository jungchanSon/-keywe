package com.ssafy.keywe.webrtc.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.ssafy.keywe.BuildConfig
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
import javax.inject.Inject

@HiltViewModel
class KeyWeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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

    init {
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
                    }

                    override fun onRtcStats(stats: RtcStats?) {
                        super.onRtcStats(stats)
                        Log.d("ScreenSharing", "onRtcStats: $stats")
                        _localStats.update {
                            it?.copy(rtcStats = stats)
                        }
                    }

                    override fun onLocalVideoStats(
                        source: Constants.VideoSourceType?,
                        stats: LocalVideoStats?,
                    ) {
                        super.onLocalVideoStats(source, stats)
                        Log.d("ScreenSharing", "onLocalVideoStats: $stats")
                        _localStats.update {
                            it?.copy(localVideoStats = stats)
                        }
                    }

                    override fun onLocalAudioStats(stats: LocalAudioStats?) {
                        super.onLocalAudioStats(stats)
                        Log.d("ScreenSharing", "onLocalAudioStats: $stats")
                        _localStats.update {
                            it?.copy(localAudioStats = stats)
                        }
                    }

                    override fun onRemoteVideoStats(stats: RemoteVideoStats?) {
                        super.onRemoteVideoStats(stats)
                        val uid = stats?.uid ?: return
                        Log.d("ScreenSharing", "onRemoteVideoStats: $stats")
                        if (_isKiosk && _screenUid.value == uid) {
                            _remoteStats.update {
                                it?.copy(remoteVideoStats = stats)
                            }
                        }
                    }

                    override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
                        super.onRemoteAudioStats(stats)
                        val uid = stats?.uid ?: return
                        Log.d("ScreenSharing", "onRemoteAudioStats: $stats")
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
                        Log.d("ScreenSharing", "onLocalVideoStateChanged: $source $state $reason")
                        if (source == Constants.VideoSourceType.VIDEO_SOURCE_SCREEN_PRIMARY) {
                            if (state == Constants.LOCAL_VIDEO_STREAM_STATE_CAPTURING) {
//                                isScreenSharing = true
                            } else if (state == Constants.LOCAL_VIDEO_STREAM_STATE_FAILED || state == Constants.LOCAL_VIDEO_STREAM_STATE_STOPPED) {
//                                isScreenSharing = false
                            }
                        }
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
            }.apply {
                createDataStream(true, true)
            }
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
}