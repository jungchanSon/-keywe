package com.ssafy.keywe.presentation.order.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.R
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel

@Composable
fun MenuImage(
    menuId: Long,
    viewModel: MenuViewModel
) {
    val menu = viewModel.getMenuSimpleModelById(menuId)
    Log.d("이미지 확인용", "$menu")
    val menuImage = menu?.image ?: ""

    if (menuImage.isNotBlank()) {
        // Base64 데이터 처리
        Base64Image(base64String = menuImage)
    } else {
        // 기본 이미지 (americano) 표시
        DefaultMenuImage()
    }
}