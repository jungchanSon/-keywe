package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.isOnlyCurrentScreenInBackStack
import com.ssafy.keywe.presentation.order.component.MenuCartDeleteDialog
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultMenuScreen(
    navController: NavController,
    menuViewModel: MenuViewModel = hiltViewModel(),
    menuCartViewModel: MenuCartViewModel = hiltViewModel(),
    storeId: Long,
) {

    var showDialog by remember { mutableStateOf(false) }

    BackHandler {
        showDialog = true
    }


    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(
                color = if (showDialog) titleTextColor.copy(
                    alpha = 0.5f
                ) else Color.Transparent
            )
    ) {
        if (showDialog) {
            MenuCartDeleteDialog(title = "주문 종료",
                description = "주문 종료시 장바구니는 초기화 됩니다.",
                onCancel = {
                    showDialog = false
                },
                onConfirm = {
                    /* 너의 action */
                    showDialog = false
                    navController.popBackStack()
                })
        }
    }

    Scaffold(topBar = {


        TopAppBar(
            backgroundColor = whiteBackgroundColor,
            elevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0),
            title = { Text(text = "주문하기", style = h6) },
            navigationIcon = {
                if (!isOnlyCurrentScreenInBackStack(navController)) {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            },
        )
    }, modifier = Modifier.fillMaxSize(), floatingActionButton = {
        DefaultFloatingCartButton(navController, menuCartViewModel, storeId)
    }) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {

            MenuCategoryScreen(menuViewModel, storeId = storeId)
            MenuSubCategory("Popular Menu")

            MenuMenuList(
                navController = navController,
                menuViewModel,
                menuCartViewModel,
                storeId = storeId,
                keyWeViewModel = hiltViewModel()
            )
        }
    }
}

@Composable
fun DefaultFloatingCartButton(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    storeId: Long,

    ) {
    val cartItems by menuCartViewModel.cartItems.collectAsState()
    val cartItemsCount = cartItems.sumOf { it.quantity }

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .size(48.dp)
            .background(whiteBackgroundColor, shape = CircleShape)
            .shadow(4.dp, CircleShape, clip = false)
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Route.MenuBaseRoute.DefaultMenuCartRoute(storeId)) },
            containerColor = Color.White,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .size(48.dp)
                .border(2.dp, primaryColor, CircleShape)
        ) {
            Image(
                modifier = Modifier.background(whiteBackgroundColor),
                painter = painterResource(R.drawable.outline_shopping_cart_24),
                contentDescription = "Cart Button"
            )
        }
    }

    if (cartItemsCount > 0) {
        Box(
            modifier = Modifier
                .size(17.dp)
                .offset(30.dp, 0.dp)
                .background(primaryColor, CircleShape), contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (cartItemsCount < 100) cartItemsCount.toString() else "99+",
                color = whiteBackgroundColor,
                fontSize = if (cartItemsCount < 100) 12.sp else 9.sp,
                modifier = Modifier.offset(y = (-3).dp)
            )
        }
    }
}

