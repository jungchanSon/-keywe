package com.ssafy.keywe.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.BottomRoute
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.domain.profile.GetProfileListModel
import com.ssafy.keywe.presentation.fcm.viewmodel.FCMViewModel
import com.ssafy.keywe.presentation.profile.component.Profile
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.subtitle2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val PARENT = "PARENT"
const val CHILD = "CHILD"


@Composable
fun ProfileChoiceScreen(
    navController: NavController,
    isJoinApp: Boolean,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    fcmViewModel: FCMViewModel = hiltViewModel(),
) {
    val profiles by profileViewModel.profiles.collectAsStateWithLifecycle() // 프로필뷰모델에서 프로필 목록을 자동으로 업데이트하기
    val parentProfiles = profiles.filter { it.role == PARENT }
    val childProfiles = profiles.filter { it.role == CHILD }
    val scope = rememberCoroutineScope()
    //화면이 다시 나타날떄마다 api 다시 조회
    LaunchedEffect(Unit) {
        profileViewModel.refreshProfileList()
    }


    Scaffold(
        topBar = {
            DefaultAppBar(
                title = if (isJoinApp) "프로필 선택" else "계정 관리", navController = navController
            )
        }, modifier = Modifier.fillMaxSize()
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
//                Text("isJoin $isJoinApp")
                Box(
                    modifier = Modifier
                        .size(120.dp)
//                        .background(color = lightColor)
                        .clickable {
                            navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)
                        }, contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profileplus),
                        contentDescription = "계정 추가",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "계정 추가", style = subtitle2
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
//                Text("isJoin $isJoinApp")
                // 부모 프로필 섹션
                if (parentProfiles.isNotEmpty()) {
                    Text(
                        text = "부모님",
                        style = h6sb,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    ProfileGrid(
                        profiles = parentProfiles, onProfileClick = { profile ->
                            // 처음
                            if (isJoinApp) {
                                joinHome(
                                    profileViewModel, profile, navController, scope
                                )
                            } else {
                                navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute)
                            }

                        }, modifier = Modifier.fillMaxWidth()
                    )
                }

                // 자녀 프로필 섹션
                if (childProfiles.isNotEmpty() || parentProfiles.isNotEmpty()) {
                    Text(
                        text = "자녀",
                        style = h6sb,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    ProfileGrid(
                        profiles = childProfiles, onProfileClick = { profile ->
                            // 처음
                            if (isJoinApp) {
                                joinHome(
                                    profileViewModel, profile, navController, scope
                                )
                            } else {
                                navController.navigate(Route.ProfileBaseRoute.ProfileEditRoute)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // 프로필 추가 버튼 (조건부 표시)
                if (parentProfiles.isEmpty() || childProfiles.size < 4) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    navController.navigate(Route.ProfileBaseRoute.ProfileAddRoute)
                                }
//                                .background(color = lightColor)
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profileplus),
                                contentDescription = "계정 추가",
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "계정 추가", style = subtitle2
                        )
                    }
                }
            }
        }
    }
}

private fun joinHome(
    profileViewModel: ProfileViewModel,
    profile: GetProfileListModel,
    navController: NavController,
    scope: CoroutineScope,
) {
    scope.launch {
        profileViewModel.selectAccount(profile)
        ProfileIdManager.updateProfileId(profile.id.toLong())
        navController.navigate(BottomRoute.ProfileRoute, builder = {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        })
    }

}


@Composable
fun ProfileGrid(
    profiles: List<GetProfileListModel>,
    onProfileClick: (GetProfileListModel) -> Unit,
//    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(profiles) { profile ->
            Profile(name = profile.name,
//                profileImage = profile.image ?: R.drawable.humanimage.toString(),
                profileImage = profile.image, // Base64 이미지 전달
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick(profile) })
        }
    }
}
