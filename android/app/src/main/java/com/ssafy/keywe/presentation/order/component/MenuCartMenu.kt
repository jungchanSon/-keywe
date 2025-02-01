package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.ssafy.keywe.presentation.order.CartItem
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.subtitle2

@Composable
fun MenuCartMenuBox(cartItem: CartItem, onDelete: (Int) -> Unit, onQuantityChange: (Int, Int) -> Unit) {
    val showDeleteDialog = remember { mutableStateOf(false) }

    Box {
        Column {
            // x버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(16.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .width(16.5.dp)
                            .height(16.5.dp)
                            .clickable { showDeleteDialog.value = true },
                        painter = painterResource(R.drawable.x),
                        contentDescription = "x"
                    )
                }
            }
            // 이미지 + 이름 + 가격
            MenuCartMenu(cartItem, onQuantityChange)
        }

        if (showDeleteDialog.value) {
            MenuCartDeleteDialog(
                title = "상품 취소",
                description = "장바구니에서 삭제하시겠습니까?",
                onCancel = { showDeleteDialog.value = false },
                onConfirm = {
                    showDeleteDialog.value = false
                    onDelete(cartItem.id)
                }
            )
        }
    }
}



@Composable
fun MenuCartMenu(cartItem: CartItem, onQuantityChange: (Int, Int) -> Unit) {
    val quantity = remember { mutableStateOf(cartItem.quantity) }

    Box(modifier = Modifier
        .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cartItem.imageURL),
                contentDescription = "Menu Image",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clip(CircleShape), // 이미지를 원형으로 자름
                contentScale = ContentScale.Crop // 이미지를 원에 맞게 잘라냄
            )

            Box() {
                MenuCartMenuInfo(cartItem.name, cartItem.price, cartItem.quantity)
            }

            Box(modifier = Modifier.height(24.dp)) {
                OptionAmount(
                    optionAmount = quantity.value,
                    onDecrease = {
                        if (quantity.value > 1) {
                            quantity.value--
                            onQuantityChange(cartItem.id, quantity.value) // 업데이트 로직 호출
                        }
                    },
                    onIncrease = {
                        quantity.value++
                        onQuantityChange(cartItem.id, quantity.value) // 업데이트 로직 호출
                    }
                )
            }
        }
    }
}

@Composable
fun MenuCartMenuInfo(name: String, price: Int, optionCount: Int) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(122.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(name)
            Box(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "${price}원",
                        style = subtitle2.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.em)
                    )
                    VerticalDivider()
                    Text(
                        text = "${optionCount}개의 옵션",
                        style = caption.copy(fontSize = 12.sp, letterSpacing = 0.em),
                        color = polishedSteelColor
                    )

                }
            }
        }
    }
}