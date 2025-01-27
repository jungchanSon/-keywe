package com.ssafy.keywe.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.MenuButton
import com.ssafy.keywe.R
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.profile.OrderStaticsBox
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.subtitle1


@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = { DefaultAppBar(title = "프로필") },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            // 프로필 상단 섹션
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.humanimage),
                    contentDescription = "프로필이미지",
                    modifier = Modifier
                        .size(80.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "김동철",
                        style = h6sb,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Keywe@ssafy.com",
                        style = subtitle1,
                        color = Color.Gray
                    )
                }
            }

            // 오더 통계 카드
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OrderStaticsBox(
                    modifier = Modifier.weight(1f),
                    orderCount = 39,
                    label = "Total Orders"
                )
                OrderStaticsBox(
                    modifier = Modifier.weight(1f),
                    orderCount = 2,
                    label = "Active Orders"
                )
                OrderStaticsBox(
                    modifier = Modifier.weight(1f),
                    orderCount = 39,
                    label = "Cancle Orders"
                )
            }

//            // 계정관리 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {}
            MenuButtonComponent()
        }
    }
}


@Composable
fun MenuButtonComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MenuButton(
            text = "본인정보",
            onClick = {}
        )
        MenuButton(
            text = "계정 관리",
            onClick = {}
        )
        MenuButton(
            text = "도움말",
            onClick = {}
        )
        MenuButton(
            text = "로그아웃",
            onClick = {}
        )
        MenuButton(
            text = "회원탈퇴",
            onClick = {}
        )
    }
}
