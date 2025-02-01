package com.ssafy.keywe.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.HorizontalDivider
import com.ssafy.keywe.presentation.order.component.MenuCartBottom
import com.ssafy.keywe.presentation.order.component.MenuCartMenuBox
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    val quantity: Int,
    val imageURL: String,
)

@Composable
fun MenuCartScreen(navController: NavController) {

    var cartItems = remember {
        mutableStateListOf(
            CartItem(
                1, "아메리카노", 3000, 1, "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"
            ), CartItem(
                2, "카페라떼", 3500, 1, "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"
            ), CartItem(
                3, "카푸치노", 4000, 1, "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"
            )
        )
    }

    fun removeItem(itemId: Int) {
        cartItems.removeAll { it.id == itemId }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        val index = cartItems.indexOfFirst { it.id == itemId }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = newQuantity)
        }
    }

    Scaffold(
        topBar = { DefaultAppBar(title = "장바구니", navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(whiteBackgroundColor),
        ) {
            // 상단 - 스크롤이 가능한 장바구니 항목 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(vertical = 24.dp),
            ) {
                items(cartItems) { item ->
                    MenuCartMenuBox(cartItem = item,
                        onDelete = { removeItem(it) },
                        onQuantityChange = { id, newQuantity -> updateQuantity(id, newQuantity) })

                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 10.dp),
                    ) {
                        HorizontalDivider()
                    }
                }
            }

            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .background(whiteBackgroundColor)
                    .align(Alignment.BottomCenter),
            ) {
                MenuCartBottom(cartItems.sumOf { it.quantity }, cartItems.sumOf { it.price * it.quantity })
            }
        }
    }
}
