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
    val partnerName: String?,
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

interface Message {
    val type: MessageType
}

@Serializable
sealed class MessageData {
    abstract val mType: MessageType
}


enum class MessageType {
    BUTTON_EVENT, DRAG_EVENT
}

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
    data class CartIdOpenDialog(val cartId: Long) : KeyWeButtonEvent()

    @Serializable
    data class CartIdOpenBottomSheet(val cartId: Long) : KeyWeButtonEvent()

    @Serializable
    data object CartCloseBottomSheet : KeyWeButtonEvent()

    @Serializable
    data object CartAcceptBottomSheet : KeyWeButtonEvent()

    @Serializable
    data object BackButton : KeyWeButtonEvent()

    @Serializable
    data object StoreButton : KeyWeButtonEvent()

    @Serializable
    data object OrderButton : KeyWeButtonEvent()

    @Serializable
    data class MenuDetailSelectCommonOption(
        val optionValue: String,
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuDetailPlusExtraOption(
        val optionName: String,
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuDetailMinusExtraOption(
        val optionName: String,
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuCartPlusAmount(
        val cartItemId: Long,
    ) : KeyWeButtonEvent()

    @Serializable
    data class MenuCartMinusAmount(
        val cartItemId: Long,
    ) : KeyWeButtonEvent()


    @Serializable
    data class ScrollEvent(
        val firstVisibleItemIndex: Int,
        val firstVisibleItemScrollOffset: Int,
    ) : KeyWeButtonEvent()

    override var mType: MessageType = MessageType.BUTTON_EVENT
}

@Serializable
data class ButtonEventMessage(
    override val type: MessageType = MessageType.BUTTON_EVENT,
    val eventData: String,
) : Message



