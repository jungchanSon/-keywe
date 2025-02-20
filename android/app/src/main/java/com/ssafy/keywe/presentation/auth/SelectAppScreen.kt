package com.ssafy.keywe.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.kiosk.component.SelectOptionCard
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.logo
import com.ssafy.keywe.ui.theme.primaryColor

@Composable
fun SelectAppScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(Modifier.padding(horizontal = 32.dp), verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "KEYWE",
                    style = logo,  // ✅ 로그인 화면과 동일한 스타일 적용
                    color = primaryColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    "KEYWE는 어르신들이\n" + "키오스크를 쉽게 사용하도록\n" +
                            "대리주문 원격 조정 서비스를 제공합니다.",
                    style = h6sb,
                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center

                )

                Spacer(modifier = Modifier.height(50.dp))

                Text("키위 이용자 선택하기", style = h6)


//                Text(
//                    "사장님 고객은 “사장님으로 시작하기”를,\n" + "이용자 고객님은 “이용자로 시작하기를” 선택해주세요.  ",
//                    style = subtitle1
//                )

                Spacer(modifier = Modifier.height(10.dp))

//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .scale(1.2f) // ✅ 크기 1.2배 확대
//                )
                SelectOptionCard(
                    title = "사장님으로 시작하기",
                    description = "메뉴선택, 편집 및 관리",
                    imageRes = R.drawable.ceo,
                    backgroundColor = Color(0xFFFFEFCB),
                    onClick = {
                        navController.navigate(
                            Route.AuthBaseRoute.CeoLoginRoute
                        )
                    },


                    )




                Spacer(modifier = Modifier.height(8.dp))


                SelectOptionCard(title = "이용자로 시작하기",
                    description = "키위매칭 서비스 이용",
                    imageRes = R.drawable.user,
                    backgroundColor = Color(0xFFD9F1D2),
                    onClick = {
                        navController.navigate(
                            Route.AuthBaseRoute.LoginRoute
                        )
                    })


            }

        }
    }
}