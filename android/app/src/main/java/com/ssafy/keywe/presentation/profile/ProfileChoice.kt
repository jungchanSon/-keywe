package com.ssafy.keywe.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.profile.component.Profile


@Composable
fun ProfileChoice(navController: NavController) {
    Scaffold(
        topBar = { DefaultAppBar(title = "프로필", navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 메인 프로필
            Profile(
                name = "김동철",
                modifier = Modifier.padding(20.dp)
            )

            // 첫번째 행
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Profile(
                    name = "김싸피",
                    modifier = Modifier.weight(1f)
                )
                Profile(
                    name = "이싸피",
                    modifier = Modifier.weight(1f)
                )
            }

            // 두번째 행
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Profile(
                    name = "박싸피",
                    modifier = Modifier.weight(1f)
                )
                Profile(
                    name = "정싸피",
                    modifier = Modifier.weight(1f)
                )
            }
            // 추가하기 아이콘
            Image(
                painter = painterResource(id = R.drawable.profileplus),
                contentDescription = "프로필 추가",
                modifier = Modifier
                    .padding(top = 60.dp)
                    .size(48.dp)
                    .clickable {/* 여기에 다음 페이지 넘어가도록 하기 */ }
            )
        }
    }
}


