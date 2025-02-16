package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultMenuScreen(
    navController: NavController,
    menuViewModel: MenuViewModel = hiltViewModel(),
    menuCartViewModel: MenuCartViewModel,
    storeId: Long,
) {
    Log.d("MenuSCreen", "storeId = $storeId")

    Scaffold(
//        topBar = {
//            DefaultOrderAppBar(
//                title = "주문하기",
//                navController = navController,
//                viewModel = appBarViewModel,
//                keyWeViewModel = keyWeViewModel,
//                isRoot = true
//            )
//        },
        modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingCartButton(navController, menuCartViewModel, storeId)
        }) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            MenuCategoryScreen(menuViewModel, storeId)
            MenuSubCategory("Popular Coffee")

            MenuMenuList(
                navController = navController, menuViewModel, menuCartViewModel, storeId = storeId
            )
        }
    }
}



