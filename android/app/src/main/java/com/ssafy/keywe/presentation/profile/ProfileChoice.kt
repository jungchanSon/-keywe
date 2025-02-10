package com.ssafy.keywe.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.data.dto.profile.ProfileData
import com.ssafy.keywe.presentation.profile.component.Profile
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.viewmodel.AddMemberViewModel

@Composable
fun ProfileGrid(
    profiles: List<ProfileData>,
    onProfileClick: (ProfileData) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // 메인 프로필 (2칸 차지)
        if (profiles.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Profile(
                    name = profiles[0].name,
                    profileImage = profiles[0].profileImage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProfileClick(profiles[0]) }
                )
            }
        }

        // 나머지 프로필들
        items(
            count = profiles.drop(1).size,
            key = { index -> index }  // 인덱스를 키로 사용
        ) { index ->
            val profile = profiles[index + 1]  // 첫 번째 프로필을 제외한 나머지
            Profile(
                name = profile.name,
                profileImage = profile.profileImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick(profile) }
            )
        }

        // 추가 버튼
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAddClick)
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profileplus),
                    contentDescription = "계정 추가",
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "계정 추가",
                    style = subtitle2
                )
            }
        }
    }
}

// ProfileChoice 화면 개선
@Composable
fun ProfileChoice(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    addMemberViewModel: AddMemberViewModel = hiltViewModel()
) {
    val profiles = profileViewModel.profiles.collectAsState().value

    LaunchedEffect(Unit) {
        addMemberViewModel.profileAddedEvent.collect { newProfile ->
            profileViewModel.addProfile(newProfile)
        }
    }

    Scaffold(
        topBar = { DefaultAppBar(title = "계정 관리", navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (profiles.isEmpty()) {
            // 프로필이 없을 때의 UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profileplus),
                    contentDescription = "계정 추가",
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute) }
                )
                Text(
                    text = "계정 추가",
                    style = subtitle2,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            // 프로필 그리드 표시
            ProfileGrid(
                profiles = profiles,
                onProfileClick = {
                    navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute)
                },
                onAddClick = {
                    navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}