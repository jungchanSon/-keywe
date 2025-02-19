package com.ssafy.keywe.presentation.order.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.keywe.R
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel

@Composable
fun MenuCartMenuBox(
    cartItem: MenuCartViewModel.CartItem,
    viewModel: MenuCartViewModel,
    storeId: Long,
    keyWeViewModel: KeyWeViewModel = hiltViewModel(),
    isKiosk: Boolean = false,
) {
    Box {
        Column {
            // x버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(modifier = Modifier
                        .width(16.5.dp)
                        .height(16.5.dp)
                        .semantics { contentDescription = "cart_open_dialog_${cartItem.id}" }
                        .noRippleClickable {
                            viewModel.openDeleteDialog(cartItem)
                            if (!isKiosk) keyWeViewModel.sendButtonEvent(
                                KeyWeButtonEvent.CartIdOpenDialog(
                                    cartItem.id
                                )
                            )
                        }, painter = painterResource(R.drawable.x), contentDescription = "x"
                    )
                }
            }
            // 이미지 + 이름 + 가격
            MenuCartMenu(cartItem, viewModel, storeId, keyWeViewModel, isKiosk)
        }
    }
}

@Composable
fun MenuCartMenu(
    cartItem: MenuCartViewModel.CartItem,
    viewModel: MenuCartViewModel,
    storeId: Long,
    keyWeViewModel: KeyWeViewModel,
    isKiosk: Boolean = false,
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val updatedCartItem = cartItems.find { it.id == cartItem.id }
    val quantity = updatedCartItem?.quantity ?: cartItem.quantity
    val isOptionChangeSheetOpen = remember { mutableStateOf(false) }

    val name = cartItem.name
    val price = cartItem.price
    val size = cartItem.size
    val image = cartItem.image ?: ""
    val temperature = cartItem.temperature
    val extraOptions = remember(cartItem) {
        mutableStateMapOf<Long, Pair<String, Int>>().apply {
            cartItem.extraOptions.forEach { (optionId, pair) ->
                put(optionId, pair)
            }
        }
    }
    Log.d("extraOptions", "$extraOptions")

//    LaunchedEffect(cartItem.menuId) {
//        Log.d("MenuCartViewModel", "스크린쪽 호출")
//        viewModel.fetchMenuDetailById(cartItem.menuId, storeId)
//    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val modifierCartImage = Modifier
                .height(60.dp)
                .width(60.dp)
                .clip(CircleShape)

            Base64Image(modifier = modifierCartImage, image)

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = name, style = subtitle2.copy(
                                fontWeight = FontWeight.Bold, letterSpacing = 0.sp
                            )
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "크기: $size, 온도: $temperature", style = caption.copy(
                                    letterSpacing = 0.sp
                                ), color = polishedSteelColor
                            )

                            extraOptions.forEach { (optionValueId, value) ->
                                val (optionValue, count) = value

                                if (optionValue.isNotBlank() && count > 0) { // 빈 값과 0개인 옵션 제외
                                    Text(
                                        text = "$optionValue: $count",
                                        style = caption.copy(letterSpacing = 0.sp),
                                        color = polishedSteelColor
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Row(
                                modifier = Modifier.height(18.12.dp),
                                horizontalArrangement = Arrangement.spacedBy(
                                    14.dp, Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "${price}원", style = subtitle2.copy(
                                        fontWeight = FontWeight.Bold, letterSpacing = 0.em
                                    )
                                )
                                VerticalDivider()
                                Text(text = "옵션 변경",
                                    style = caption.copy(fontSize = 14.sp, letterSpacing = 0.em),
                                    color = polishedSteelColor,
                                    modifier = Modifier
                                        .semantics {
                                            contentDescription =
                                                "cart_open_bottom_sheet_${cartItem.id}"
                                        }
                                        .noRippleClickable {
                                            isOptionChangeSheetOpen.value = true
                                            if (!isKiosk) keyWeViewModel.sendButtonEvent(
                                                KeyWeButtonEvent.CartIdOpenBottomSheet(cartItem.id)
                                            )
                                        })
                            }

                            Box(modifier = Modifier.height(24.dp)) {
                                CartMenuAmount(optionAmount = quantity, onDecrease = {
                                    if (quantity > 1) {
                                        viewModel.updateCartQuantity(
                                            cartItem.id, quantity - 1
                                        ) // 업데이트 로직 호출
                                    }
                                    if (!isKiosk) keyWeViewModel.sendButtonEvent(
                                        KeyWeButtonEvent.MenuCartMinusAmount(
                                            cartItem.id
                                        )
                                    )
                                }, onIncrease = {
                                    viewModel.updateCartQuantity(
                                        cartItem.id, quantity + 1
                                    ) // 업데이트 로직 호출
                                    if (!isKiosk) keyWeViewModel.sendButtonEvent(
                                        KeyWeButtonEvent.MenuCartPlusAmount(
                                            cartItem.id
                                        )
                                    )
                                },
                                    cartItemId = cartItem.id
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (isOptionChangeSheetOpen.value) {
        OptionChangeBottomSheet(
            cartItem = cartItem,
            viewModel = viewModel,
            onDismiss = { isOptionChangeSheetOpen.value = false },
            storeId,
            keyWeViewModel,
            isKiosk
        )
    }

}

@Composable
fun CartMenuAmount(
    cartItemId: Long,
    optionAmount: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Row(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .semantics { contentDescription = "minus_amount_$cartItemId" }
                .noRippleClickable { onDecrease() },
            painter = painterResource(R.drawable.minus_circle),
            contentDescription = "minus in circle"
        )
        Text(
            text = "$optionAmount", style = subtitle1
        )
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .semantics { contentDescription = "plus_amount_$cartItemId" }
                .noRippleClickable { onIncrease() },
            painter = painterResource(R.drawable.profileplus),
            contentDescription = "plus in circle"
        )
    }
}