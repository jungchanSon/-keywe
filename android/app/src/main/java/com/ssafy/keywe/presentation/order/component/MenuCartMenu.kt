package com.ssafy.keywe.presentation.order.component

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.R
import com.ssafy.keywe.presentation.order.viewmodel.CartItem
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.subtitle2

@Composable
fun MenuCartMenuBox(
    cartItem: CartItem,
    viewModel: MenuViewModel,
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
                    Image(
                        modifier = Modifier
                            .width(16.5.dp)
                            .height(16.5.dp)
                            .noRippleClickable { viewModel.openDeleteDialog(cartItem) },
                        painter = painterResource(R.drawable.x),
                        contentDescription = "x"
                    )
                }
            }
            // 이미지 + 이름 + 가격
            MenuCartMenu(cartItem, viewModel)
        }
    }
}

@Composable
fun MenuCartMenu(cartItem: CartItem, viewModel: MenuViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val updatedCartItem = cartItems.find { it.id == cartItem.id }
    val quantity = updatedCartItem?.quantity ?: cartItem.quantity
    val isOptionChangeSheetOpen = remember { mutableStateOf(false) }

    val name = cartItem.name
    val price = cartItem.price
    val size = cartItem.size
    val temperature = cartItem.temperature
    val extraOptions = cartItem.extraOptions

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cartItem.imageURL),
                contentDescription = "Menu Image",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

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
                            text = name,
                            style = subtitle2.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.sp
                            )
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ){
                            Text(
                                text = "크기: $size, 온도: $temperature",
                                style = caption.copy(
                                    letterSpacing = 0.sp
                                ),
                                color = polishedSteelColor
                            )

                            extraOptions.forEach { (optionName, count) ->
                                Text(
                                    text = "$optionName: $count",
                                    style = caption.copy(
                                        letterSpacing = 0.sp
                                    ),
                                    color = polishedSteelColor
                                )
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
                                modifier = Modifier
                                    .height(18.12.dp),
                                horizontalArrangement = Arrangement.spacedBy(
                                    14.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "${price}원",
                                    style = subtitle2.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.em
                                    )
                                )
                                VerticalDivider()
                                Text(
                                    text = "옵션 변경",
                                    style = caption.copy(fontSize = 14.sp, letterSpacing = 0.em),
                                    color = polishedSteelColor,
                                    modifier = Modifier.noRippleClickable { isOptionChangeSheetOpen.value = true }
                                )
                            }

                            Box(modifier = Modifier.height(24.dp)) {
                                CartMenuAmount(
                                    optionAmount = quantity,
                                    onDecrease = {
                                        if (quantity > 1) {
                                            viewModel.updateCartQuantity(
                                                cartItem.id,
                                                quantity - 1
                                            ) // 업데이트 로직 호출
                                        }
                                    },
                                    onIncrease = {
                                        viewModel.updateCartQuantity(
                                            cartItem.id,
                                            quantity + 1
                                        ) // 업데이트 로직 호출
                                    }
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
            onDismiss = { isOptionChangeSheetOpen.value = false }
        )
    }

}

@Composable
fun CartMenuAmount(optionAmount: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
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
                .noRippleClickable { onDecrease() },
            painter = painterResource(R.drawable.minus_circle),
            contentDescription = "minus in circle"
        )
        Text(
            text = "$optionAmount",
            style = subtitle1
        )
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .noRippleClickable { onIncrease() },
            painter = painterResource(R.drawable.profileplus),
            contentDescription = "plus in circle"
        )
    }
}