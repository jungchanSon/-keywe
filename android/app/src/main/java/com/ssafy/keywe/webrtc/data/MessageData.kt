package com.ssafy.keywe.webrtc.data

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
enum class STOMPTYPE {
    // TODO SUBSCRIBE screen 쪽에 수정
    REQUESTED, SUBSCRIBE, WAITING, ACCEPTED, TIMEOUT, END, ERROR
}

@Serializable
data class WebSocketMessage(
    val type: STOMPTYPE,
    val data: STOMPData?,
    val timestamp: String,
)


@Serializable
data class STOMPData(
    val success: Int?,
    val failure: Int?,
    val sessionId: String?,
    val helperUserId: String?,
    val kioskUserId: String?,
    val channel: ChannelData?,
    val message: String?,
    val code: String?,
)

@Serializable
data class ChannelData(
    val name: String,
    val token: String,
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
//enum class MessageType {
//    ScreenSize, Touch, Drag, RequestScreenSize
//}

//(val type: MessageType, open val x: Float, open val y: Float)


//@Serializable
//sealed class MessageData {
//    abstract val messageType: MessageType
//    abstract val x: Float
//    abstract val y: Float
//}

//@Serializable
//@SerialName("Touch")
//data class Touch(
//    override val messageType: MessageType,
//    override val x: Float,
//    override val y: Float,
//) : MessageData() {
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

//@Serializable
//@SerialName("Drag")
//data class Drag(
//    override val messageType: MessageType,
//    override val x: Float,
//    override val y: Float,
//) : MessageData() {
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


//@Serializable
//@SerialName("ScreenSize")
//data class ScreenSize(
//    override val messageType: MessageType,
//    override val x: Float,
//    override val y: Float,
//    val density: Float,
//    val aspectRatio: Float = y.toFloat() / x,
//) : MessageData() {
//    fun convertGestureToAnotherScreen(
//        remoteScreen: ScreenSize,    // B 화면
//        offset: Offset,  // 상대 화면에서의 터치한 offset 좌표
//    ): Offset {
//        // A 화면의 터치 좌표를 비율로 변환한 뒤 B "화면에 맞게 비율을 적용한 새로운 좌표 계산
//        Log.d(
//            "sendGesture",
//            "x ratio: ${(offset.x / remoteScreen.x)}, y ratio: ${(offset.y / remoteScreen.y)}"
//        )
//        val x = (offset.x / remoteScreen.x) * this.x
//        val y = (offset.y / remoteScreen.y) * this.y
//        return Offset(x, y)
//    }
//}


interface Message {
    val type: MessageType
}

@Serializable
sealed class MessageData {
    abstract val mType: MessageType
}


enum class MessageType {
    BUTTON_EVENT
}
//
//@Serializable
//enum class STOMPTYPE {
//    // TODO SUBSCRIBE screen 쪽에 수정
//    REQUESTED, SUBSCRIBE, WAITING, ACCEPTED, TIMEOUT, END, ERROR
//}
//
//@Serializable
//data class STOMPData(
//    val success: Int?,
//    val failure: Int?,
//    val sessionId: String?,
//    val helperUserId: String?,
//    val kioskUserId: String?,
//    val channel: ChannelData?,
//    val message: String?,
//    val code: String?,
//)
//
//@Serializable
//data class ChannelData(
//    val name: String,
//    val token: String,
//)
//
//@Serializable
//data class WebSocketMessage(
//    val type: STOMPTYPE,
//    val data: STOMPData?,
//    val timestamp: String,
//)

@Serializable
sealed class KeyWeButtonEvent : MessageData() {
    @Serializable
    data class CategorySelect(
        val categoryId: Long,
        val categoryName: String,
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuSelect(
        val menuId: Long,
        val storeId: Long,
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuAddToCart(
        val menuId: Long,
    ) : KeyWeButtonEvent()

    @Serializable
    data object MenuCart : KeyWeButtonEvent()


    @Serializable
    data object CartOpenDialog : KeyWeButtonEvent()

    @Serializable
    data object CartCloseDialog : KeyWeButtonEvent()

    @Serializable
    data object CartAcceptDialog : KeyWeButtonEvent()


    @Serializable
    data class MenuDetailSelectCommonOption(
        val optionValue: String
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuDetailPlusExtraOption(
        val optionName: String
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuDetailMinusExtraOption(
        val optionName: String
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuCartPlusAmount(
        val cartItemName: String
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuCartMinusAmount(
        val cartItemName: String
    ) : KeyWeButtonEvent()

    override val mType: MessageType = MessageType.BUTTON_EVENT
}

@Serializable
data class ButtonEventMessage(
    override val type: MessageType = MessageType.BUTTON_EVENT,
    val eventData: String,
) : Message



