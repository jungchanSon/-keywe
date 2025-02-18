package com.ssafy.keywe.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.kiosk.component.SelectOptionCard
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.subtitle1

@Composable
fun SelectAppScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.height(65.dp))
                Text("키위 이용자 선택하기", style = h6sb)
                Spacer(modifier = Modifier.height(12.dp))
                Text("키위는 사장님과 이용자 고객님들이 사용할 수 있도록 만들었습니다.", style = subtitle1)

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "사장님 고객은 “사장님으로 시작하기”를,\n" + "이용자 고객님은 “이용자로 시작하기를” 선택해주세요.  ",
                    style = subtitle1
                )

                Spacer(modifier = Modifier.height(40.dp))

                SelectOptionCard(title = "키위 매칭 이용",
                    description = "사장님으로 시작하기\n메뉴선택, 편집 및 관리",
                    imageRes = R.drawable.ceo,
                    backgroundColor = Color(0xFFF8B195),
                    onClick = {
                        navController.navigate(
                            Route.AuthBaseRoute.CeoLoginRoute
                        )
                    })
                Spacer(modifier = Modifier.height(16.dp))

                SelectOptionCard(title = "키위 매칭 이용",
                    description = "이용자로 시작하기\n키위매칭 서비스 이용",
                    imageRes = R.drawable.user,
                    backgroundColor = Color(0xFFBFA895),
                    onClick = {
                        navController.navigate(
                            Route.AuthBaseRoute.LoginRoute
                        )
                    })

            }

        }
    }
}