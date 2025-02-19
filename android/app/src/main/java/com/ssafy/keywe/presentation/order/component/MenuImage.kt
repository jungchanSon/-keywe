package com.ssafy.keywe.presentation.order.component

import androidx.compose.runtime.Composable
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel

@Composable
fun MenuImage(
    menuId: Long,
    viewModel: MenuViewModel,
) {
    val menu = viewModel.getMenuSimpleModelById(menuId)
//    Log.d("이미지 확인용", "$menu")
    val menuImage = menu?.image ?: ""


    if (menuImage.isNotBlank()) {
        // Base64 데이터 처리
        Base64Image(base64String = menuImage)
    } else {
        // 기본 이미지 (americano) 표시
        DefaultMenuImage()
    }
}