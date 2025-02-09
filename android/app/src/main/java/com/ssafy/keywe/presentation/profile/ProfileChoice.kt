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
import com.ssafy.keywe.common.Route
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
            Profile(name = "김동철",
                modifier = Modifier
                    .padding(20.dp)
                    .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) })

            // 첫번째 행
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Profile(name = "김싸피",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) })
                Profile(name = "이싸피",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) })
            }

            // 두번째 행
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Profile(name = "박싸피",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) })
                Profile(name = "정싸피",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) })
            }
            // 추가하기 아이콘
            Image(painter = painterResource(id = R.drawable.profileplus),
                contentDescription = "프로필 추가",
                modifier = Modifier
                    .padding(top = 60.dp)
                    .size(48.dp)
                    .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute) })
        }
    }
}


//@Composable
//fun ProfileChoice(
//    navController: NavController,
//    viewModel: AddMemberViewModel = hiltViewModel()
//) {
//    val profiles = viewModel.profiles.collectAsStateWithLifecycle().value
//
//    Scaffold(
//        topBar = { DefaultAppBar(title = "계정 관리", navController = navController) },
//        modifier = Modifier.fillMaxSize()
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(innerPadding)
//                .padding(horizontal = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
//        ) {
//            if (profiles.isEmpty()) {
//                // 프로필이 없을 때 추가 버튼만 표시
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.profileplus),
//                        contentDescription = "계정 추가",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute) }
//                    )
//                    Text(
//                        text = "계정 추가",
//                        style = subtitle2,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            } else {
//                // 프로필이 있을 때 목록 표시
//                // 메인 프로필 (첫 번째 프로필)
//                profiles.firstOrNull()?.let { mainProfile ->
//                    Profile(
//                        name = mainProfile.name,
//                        modifier = Modifier
//                            .padding(vertical = 20.dp)
//                            .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) }
//                    )
//                }
//
//                // 나머지 프로필들을 2열로 표시
//                profiles.drop(1).chunked(2).forEach { rowProfiles ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 10.dp),
//                        horizontalArrangement = Arrangement.spacedBy(20.dp)
//                    ) {
//                        rowProfiles.forEach { profile ->
//                            Profile(
//                                name = profile.name,
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute) }
//                            )
//                        }
//                        // 한 행에 프로필이 하나만 있을 경우 빈 공간 추가
//                        if (rowProfiles.size == 1) {
//                            Spacer(modifier = Modifier.weight(1f))
//                        }
//                    }
//                }
//
//                // 추가 버튼
//                Image(
//                    painter = painterResource(id = R.drawable.profileplus),
//                    contentDescription = "계정 추가",
//                    modifier = Modifier
//                        .padding(top = 40.dp)
//                        .size(48.dp)
//                        .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute) }
//                )
//            }
//        }
//    }
//}
