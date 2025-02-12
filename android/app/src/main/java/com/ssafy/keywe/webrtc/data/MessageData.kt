package com.ssafy.keywe.webrtc.data

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WebSocketMessage(
    val type: String,
    val sessionId: String,
    val channelInfo: ChannelInfo?,
    val data: SessionData?,
)

@Serializable
data class ChannelInfo(
    val channelName: String,
    val token: String,
)

@Serializable
data class SessionData(
    val helperUserId: String,
    val kioskUserId: String,
)

@Serializable
@JsonClass(generateAdapter = true)
data class RequestMessage(
    val storeId: String,
)

@Serializable
@JsonClass(generateAdapter = true)
data class AcceptMessage(
    val sessionId: String,
)

@Serializable
@JsonClass(generateAdapter = true)
data class CloseMessage(
    val sessionId: String,
)


//@Serializable
//sealed class ScreenSize(val width: Float, val height: Float) {
//    @Serializable
//    data class LocalScreenSize(val w: Float, val h: Float) : ScreenSize(w, h)
//
//    @Serializable
//    data class RemoteScreenSize(val w: Float, val h: Float) : ScreenSize(w, h)
//
//    fun convertGestureToAnotherScreen(
//        remoteScreen: ScreenSize,    // B 화면
//        offset: Offset,  // A 화면에서의 터치한 offset 좌표
//    ): Offset {
//        // A 화면의 터치 좌표를 비율로 변환한 뒤 B 화면에 맞게 비율을 적용한 새로운 좌표 계산
//        val x = (offset.x / this.width) * remoteScreen.width
//        val y = (offset.y / this.height) * remoteScreen.height
//        return Offset(x, y)
//    }
//}

//@Serializable
//data class GestureData(
//    val type: MessageType,
//    val x: Float,
//    val y: Float,
//)

//fun ScreenSize.toMessage(): ScreenSizeMessage = ScreenSizeMessage(
//    type = MessageType.ScreenSize, width = this.width, height = this.height
//)

@Serializable
enum class MessageType {
    ScreenSize, Touch, Drag, RequestScreenSize
}

//(val type: MessageType, open val x: Float, open val y: Float)


@Serializable
sealed class MessageData {
    abstract val messageType: MessageType
    abstract val x: Float
    abstract val y: Float
}

@Serializable
@SerialName("Touch")
data class Touch(
    override val messageType: MessageType,
    override val x: Float,
    override val y: Float,
) : MessageData() {
    fun convertGestureToAnotherScreen(
        remoteScreen: ScreenSize,    // B 화면
        offset: Offset,  // A 화면에서의 터치한 offset 좌표
    ): Offset {
        // A 화면의 터치 좌표를 비율로 변환한 뒤 B 화면에 맞게 비율을 적용한 새로운 좌표 계산
        val x = (offset.x / this.x) * remoteScreen.x
        val y = (offset.y / this.y) * remoteScreen.y
        return Offset(x, y)
    }
}

@Serializable
@SerialName("Drag")
data class Drag(
    override val messageType: MessageType,
    override val x: Float,
    override val y: Float,
) : MessageData() {
    fun convertGestureToAnotherScreen(
        remoteScreen: ScreenSize,    // B 화면
        offset: Offset,  // A 화면에서의 터치한 offset 좌표
    ): Offset {
        // A 화면의 터치 좌표를 비율로 변환한 뒤 B 화면에 맞게 비율을 적용한 새로운 좌표 계산
        val x = (offset.x / this.x) * remoteScreen.x
        val y = (offset.y / this.y) * remoteScreen.y
        return Offset(x, y)
    }
}


@Serializable
@SerialName("ScreenSize")
data class ScreenSize(
    override val messageType: MessageType,
    override val x: Float,
    override val y: Float,
) : MessageData() {
    fun convertGestureToAnotherScreen(
        remoteScreen: ScreenSize,    // B 화면
        offset: Offset,  // 상대 화면에서의 터치한 offset 좌표
    ): Offset {
        // A 화면의 터치 좌표를 비율로 변환한 뒤 B "화면에 맞게 비율을 적용한 새로운 좌표 계산
        Log.d(
            "sendGesture",
            "x ratio: ${(offset.x / remoteScreen.x)}, y ratio: ${(offset.y / remoteScreen.y)}"
        )
        val x = (offset.x / remoteScreen.x) * this.x
        val y = (offset.y / remoteScreen.y) * this.y
        return Offset(x, y)
    }
}

//@Serializable
//sealed interface MessageData {
//    val x: Float
//    val y: Float
//
//    @SerialName("Touch")
//    data class Touch(val type: MessageType, override val x: Float, override val y: Float) :
//        MessageData
//
//    @SerialName("Drag")
//    data class Drag(val type: MessageType, override val x: Float, override val y: Float) :
//        MessageData
//
//    @SerialName("ScreenSize")
//    data class ScreenSize(val type: MessageType, override val x: Float, override val y: Float) :
//        MessageData
//
//    fun convertGestureToAnotherScreen(
//        remoteScreen: ScreenSize,    // B 화면
//        offset: Offset,  // A 화면에서의 터치한 offset 좌표
//    ): Offset {
//        // A 화면의 터치 좌표를 비율로 변환한 뒤 B 화면에 맞게 비율을 적용한 새로운 좌표 계산
//        val x = (offset.x / this.x) * remoteScreen.x
//        val y = (offset.y / this.y) * remoteScreen.y
//        return Offset(x, y)
//    }
//}

//data class ScreenSizeMessage(val type: MessageType, val width: Float, val height: Float)
