//package com.ssafy.keywe.presentation.profile.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.ssafy.keywe.data.datastore.ProfileDataStore
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ProfileIdViewModel @Inject constructor(
//    private val profileDataStore: ProfileDataStore
//) : ViewModel() {
//
//    private val _profileId = MutableStateFlow(-1L) // ✅ 선택된 profileId를 저장
//    val profileId: StateFlow<Long?> = _profileId
//
//    init {
//        viewModelScope.launch {
//            profileDataStore.profileIdFlow.collect { id ->
//                _profileId.value = id // ✅ 저장된 profileId 불러오기
//            }
//        }
//    }
//
//    fun setProfileId(id: Long) {
//        viewModelScope.launch {
//            profileDataStore.saveProfileId(id) // ✅ profileId 저장
//            _profileId.value = id // ✅ UI 업데이트
//        }
//    }
//}
