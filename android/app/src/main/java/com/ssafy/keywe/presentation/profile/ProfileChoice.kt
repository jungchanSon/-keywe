package com.ssafy.keywe.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.data.dto.profile.ProfileData
import com.ssafy.keywe.presentation.profile.component.Profile
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.lightColor
import com.ssafy.keywe.ui.theme.subtitle2


@Composable
fun ProfileChoice(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val profiles by profileViewModel.profiles.collectAsStateWithLifecycle()
    val parentProfiles = profiles.filter { it.role == "PARENT" }
    val childProfiles = profiles.filter { it.role == "CHILD" }
    val context = LocalContext.current


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
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(color = greyBackgroundColor)
                        .clickable {
                            navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profileplus),
                        contentDescription = "계정 추가",
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "계정 추가",
                    style = subtitle2
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                // 부모 프로필 섹션
                if (parentProfiles.isNotEmpty()) {
                    Text(
                        text = "부모님",
                        style = subtitle2,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
                    )
                    ProfileGrid(
                        profiles = parentProfiles,
                        onProfileClick = { profile ->
                            navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute)
                        },
                        onAddClick = {
                            if (parentProfiles.size >= 2) {
                                Toast.makeText(
                                    context,
                                    " 부모님 계정은 최대 2명까지만 추가할 수 있습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 자녀 프로필 섹션
                if (childProfiles.isNotEmpty() || parentProfiles.isNotEmpty()) {
                    Text(
                        text = "자녀",
                        style = subtitle2,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
                    )
                    ProfileGrid(
                        profiles = childProfiles,
                        onProfileClick = { profile ->
                            navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute)
                        },
                        onAddClick = {
                            if (childProfiles.size >= 4) {
                                Toast.makeText(
                                    context,
                                    "자녀 계정은 최대 4명까지만 추가할 수 있습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // 프로필 추가 버튼 (조건부 표시)
                if ((parentProfiles.size < 2 || childProfiles.size < 4)) {
                    AddProfileButton(
                        onClick = {
                            when {
                                parentProfiles.isEmpty() ->
                                    navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)

                                childProfiles.size < 4 ->
                                    navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)

                                else -> Toast.makeText(
                                    context,
                                    "더이상 계정을 추가할 수 없습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
                    )
                }
            }
        }
    }
}


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
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        modifier = modifier
    ) {
        items(profiles) { profile ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightColor)
                    .aspectRatio(1f)
            ) {
                Profile(
                    name = profile.name,
                    profileImage = profile.profileImage,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onProfileClick(profile) }
                )
            }
        }
    }
}

@Composable
private fun AddProfileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(lightColor)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.profileplus),
                contentDescription = "계정 추가",
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "계정 추가",
            style = subtitle2
        )
    }
}