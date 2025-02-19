package com.ssafy.keywe.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.R
import com.ssafy.keywe.presentation.order.component.Base64Image
import com.ssafy.keywe.ui.theme.lightColor
import com.ssafy.keywe.ui.theme.subtitle2


@Composable
fun Profile(
    name: String,
    profileImage: String?, // Base64 문자열이 전달됨
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(color = lightColor)
        ) {
            if (profileImage == "DEFAULT") {  // ✅ 기본 이미지 적용
                Image(
                    painter = painterResource(id = R.drawable.humanimage),
                    contentDescription = "기본 프로필 이미지",
                    modifier = Modifier.size(120.dp)
                )
            } else if (!profileImage.isNullOrBlank() && profileImage.length > 100) { // ✅ Base64 형식인지 검증
                Base64Image(base64String = profileImage, modifier = Modifier.size(120.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.humanimage),
                    contentDescription = "기본 프로필 이미지",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = subtitle2
        )
    }
}




