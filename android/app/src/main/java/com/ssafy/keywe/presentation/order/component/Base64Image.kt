package com.ssafy.keywe.presentation.order.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun Base64Image(modifier: Modifier = Modifier, base64String: String) {
    // "data:image/png;base64," 부분 제거
    val cleanBase64String = base64String.substringAfter("base64,", base64String)

    // Base64 URL-Safe 변환: "-" → "+" 및 "_" → "/"
    val normalizedBase64 = cleanBase64String.replace('-', '+').replace('_', '/')

    val decodedByteArray: ByteArray? = try {
        Base64.decode(normalizedBase64, Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
        Log.e("Base64Image", "Base64 디코딩 실패: ${e.message}")
        null
    }

    if (decodedByteArray == null || decodedByteArray.isEmpty()) {
        Log.e("Base64Image", "디코딩된 바이트 배열이 비어 있음.")
        DefaultMenuImage(modifier)
        return
    }

//    Log.d("Base64Image", "디코딩된 바이트 배열 길이: ${decodedByteArray.size}")

    val bitmap: Bitmap? = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)

    if (bitmap != null) {
//        Log.d("Base64Image", "Bitmap 변환 성공")
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Base64 Image",
            modifier = modifier
                .width(171.dp)
                .height(100.dp)
                .background(color = Color.Transparent),
            contentScale = ContentScale.Fit
        )
    } else {
        Log.e("Base64Image", "Bitmap 변환 실패")
        DefaultMenuImage(modifier)
    }
}

@Composable
fun Base64ProfileImage(modifier: Modifier = Modifier, base64String: String) {
    // "data:image/png;base64," 부분 제거
    val cleanBase64String = base64String.substringAfter("base64,", base64String)

    // Base64 URL-Safe 변환: "-" → "+" 및 "_" → "/"
    val normalizedBase64 = cleanBase64String.replace('-', '+').replace('_', '/')

    val decodedByteArray: ByteArray? = try {
        Base64.decode(normalizedBase64, Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
        Log.e("Base64Image", "Base64 디코딩 실패: ${e.message}")
        null
    }

    if (decodedByteArray == null || decodedByteArray.isEmpty()) {
        Log.e("Base64Image", "디코딩된 바이트 배열이 비어 있음.")
        DefaultMenuImage(modifier)
        return
    }

//    Log.d("Base64Image", "디코딩된 바이트 배열 길이: ${decodedByteArray.size}")

    val bitmap: Bitmap? = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)

    if (bitmap != null) {
//        Log.d("Base64Image", "Bitmap 변환 성공")
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Base64 Image",
            modifier = modifier
                .width(171.dp)
                .height(100.dp)
                .background(color = Color.Transparent),
            contentScale = ContentScale.Fit
        )
    } else {
        Log.e("Base64Image", "Bitmap 변환 실패")
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.humanimage),
            contentDescription = "Default Image",
            modifier = modifier
//                .width(171.dp)
//                .height(100.dp)
                .fillMaxSize()
                .background(color = Color.Transparent),
            contentScale = ContentScale.Fit
        )
    }
}
