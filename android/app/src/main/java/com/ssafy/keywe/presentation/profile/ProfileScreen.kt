package com.ssafy.keywe.presentation.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.presentation.order.component.Base64Image
import com.ssafy.keywe.presentation.profile.component.MenuButton
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileDetailViewModel
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.lightColor
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    navController: NavController,
    tokenManager: TokenManager,
    viewModel: ProfileDetailViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val profileState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(ProfileIdManager.profileId) {
        ProfileIdManager.profileId.value?.let {
            Log.d("ProfileScreen", "✅ Profile ID Loaded: $it")
            viewModel.loadProfileDetail()
            profileViewModel.refreshProfileList()
            viewModel.refreshProfile()
        }
    }


    Scaffold(
        topBar = { DefaultAppBar(title = "프로필") },
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
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(lightColor)
                ) {
                    if (!profileState.value?.image.isNullOrBlank()) {
                        Base64Image(
                            base64String = profileState.value?.image ?: "",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.humanimage),
                            contentDescription = "기본 프로필 이미지",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = profileState.value?.name ?: "",
                        style = h6sb,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp), // 두 텍스트 사이 간격 조정
                        verticalAlignment = Alignment.CenterVertically // 텍스트 높이 맞추기
                    ) {
                        Text(
                            text = when (profileState.value?.role) {
                                "PARENT" -> "부모"
                                "CHILD" -> "자녀"
                                else -> profileState.value?.role ?: ""
                            },
                            style = h6sb,
                            color = Color.Gray
                        )

                        Text(
                            text = profileState.value?.phone ?: "",
                            style = h6sb,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

//            // 계정관리 버튼
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {}
            MenuButtonComponent(navController, tokenManager)
        }
    }
}


@Composable
fun MenuButtonComponent(navController: NavController, tokenManager: TokenManager) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MenuButton(text = "프로필 수정", onClick = {
            ProfileIdManager.profileId.value?.let { profile ->
                navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute)
            }
//            navController.navigate(
//                Route.ProfileBaseRoute.ProfileChoiceRoute(false)
        })
        MenuButton(text = "로그아웃", onClick = {
            scope.launch {
                tokenManager.clearTokens()
            }
        })
        MenuButton(text = "회원탈퇴", onClick = {
            scope.launch {
                tokenManager.clearTokens()

                Toast.makeText(navController.context, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                navController.navigate(Route.AuthBaseRoute.LoginRoute) {
                    popUpTo(Route.ProfileBaseRoute.ProfileScreenRoute) { inclusive = true }
                }
            }
        })
    }
}
