package com.ssafy.keywe.presentation.order.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuCartViewModel @Inject constructor(private val repository: OrderRepository) : ViewModel() {

    private val extraOptions = listOf(
        OptionData("샷 추가", 500),
        OptionData("시럽 추가", 300),
        OptionData("바닐라 시럽 추가", 300),
        OptionData("휘핑 추가", 700),
        OptionData("샷 추가1", 500),
        OptionData("시럽 추가1", 300),
        OptionData("바닐라 시럽 추가1", 300),
        OptionData("휘핑 추가1", 700),
        OptionData("샷 추가2", 500),
        OptionData("시럽 추가2", 300),
        OptionData("바닐라 시럽 추가2", 300),
        OptionData("휘핑 추가2", 700),
        OptionData("샷 추가3", 500),
        OptionData("시럽 추가3", 300),
        OptionData("바닐라 시럽 추가3", 300),
        OptionData("휘핑 추가3", 700),
    )

    data class CartItem(
        val id: Long,
        val menuId: Long,
        val name: String,
        val price: Int,
        val quantity: Int,
        val image: String?,
        val size: String,
        val temperature: String,
        val extraOptions: Map<String, Int>
    )

    val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    val cartItemCount: StateFlow<Int> = _cartItems.map { it.sumOf { item -> item.quantity } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _isDeleteDialogOpen = MutableStateFlow(false)
    val isDeleteDialogOpen: StateFlow<Boolean> = _isDeleteDialogOpen.asStateFlow()

    private val _selectedCartItem = MutableStateFlow<CartItem?>(null)
    val selectedCartItem: StateFlow<CartItem?> = _selectedCartItem.asStateFlow()

    private val _selectedDetailMenu = MutableStateFlow<MenuDetailModel?>(null)
    val selectedDetailMenu: StateFlow<MenuDetailModel?> = _selectedDetailMenu.asStateFlow()

    fun fetchMenuDetailById(id: Long): MenuDetailModel? {
        Log.d("Fetch Menu", ":$id")

        viewModelScope.launch {
            when (val result = repository.getDetailMenu(id)) {
                is ResponseResult.Success -> {
                    _selectedDetailMenu.value = result.data
                    println("getDetailMenu 결과: ${result.data}")
                }

                is ResponseResult.ServerError -> {
                    println("서버 에러: ${result.status}")  // 서버 오류 로그
                }

                is ResponseResult.Exception -> {
                    println("예외 발생: ${result.e.message}")  // 예외 로그
                }
            }
        }
        Log.d("selectDetailMenu 메뉴:", "${selectedDetailMenu.value}")
        return selectedDetailMenu.value
    }

    fun addToCart(
        menuId: Long,
        size: String,
        temperature: String,
        extraOptions: Map<String, Int>,
        totalPrice: Int
    ) {
        val menuData = fetchMenuDetailById(menuId) ?: return

        println("장바구니 추가됨: $menuId, $size, $temperature, $extraOptions, 총 가격=$totalPrice")

        _cartItems.update { currentCart ->
            val sortedExtraOptions = extraOptions.toSortedMap()
            val existingItemIndex = currentCart.indexOfFirst {
                it.name == menuData.menuName &&
                        it.size == size &&
                        it.temperature == temperature &&
                        it.extraOptions.toSortedMap() == sortedExtraOptions
            }

            val updatedCart = currentCart.toMutableList()

            if (existingItemIndex != -1) {
                updatedCart[existingItemIndex] = updatedCart[existingItemIndex].copy(
                    quantity = updatedCart[existingItemIndex].quantity + 1
                )
            } else {
                val newId = (currentCart.maxOfOrNull { it.id } ?: 0) + 1

                updatedCart.add(
                    CartItem(
                        id = newId,
                        menuId = menuData.menuId,
                        name = menuData.menuName,
                        price = totalPrice,
                        quantity = 1,
                        image = menuData.image,
                        size = size,
                        temperature = temperature,
                        extraOptions = sortedExtraOptions
                    )
                )
            }

            updatedCart.toList()
        }
        println("현재 장바구니 상태: ${_cartItems.value}")
    }

    fun updateCartItem(
        cartItemId: Long,
        cartItemMenuId: Long,
        size: String,
        temperature: String,
        extraOptions: Map<String, Int>
    ) {
        _cartItems.update { currentCart ->
            val menuPrice = fetchMenuDetailById(cartItemMenuId)?.menuPrice ?: 0
            val sizePrice = sizePriceMap[size] ?: 0
            val sortedExtraOptions = extraOptions.toSortedMap()
            val extraOptionPrice = sortedExtraOptions.entries.sumOf { (name, quantity) ->
                val optionPrice = getExtraOptions().find { it.name == name }?.price ?: 0
                optionPrice * quantity
            }
            val newTotalPrice = menuPrice + sizePrice + extraOptionPrice

            val existingItemIndex = currentCart.indexOfFirst {
                it.menuId == cartItemMenuId &&
                        it.size == size &&
                        it.temperature == temperature &&
                        it.extraOptions == extraOptions &&
                        it.id != cartItemId // 자신과는 다른 아이템이어야 함
            }

            val updatedCart = currentCart.toMutableList()

            if (existingItemIndex != -1) {
                // 이미 동일한 항목이 존재하면 수량을 합치고 기존 아이템 삭제
                updatedCart[existingItemIndex] = updatedCart[existingItemIndex].copy(
                    quantity = updatedCart[existingItemIndex].quantity + 1
                )
                updatedCart.removeIf { it.id == cartItemId }
            } else {
                // 기존 아이템 수정 (합칠 대상이 없는 경우)
                updatedCart.replaceAll { cartItem ->
                    if (cartItem.id == cartItemId) {
                        cartItem.copy(
                            size = size,
                            temperature = temperature,
                            extraOptions = sortedExtraOptions,
                            price = newTotalPrice
                        )
                    } else {
                        cartItem
                    }
                }
            }

            updatedCart
        }

        println("현재 장바구니 상태: ${_cartItems.value}")
    }

    fun getExtraOptions(): List<OptionData> = extraOptions

    fun openDeleteDialog(cartItem: CartItem) {
        _selectedCartItem.value = cartItem
        _isDeleteDialogOpen.value = true
    }

    fun closeDeleteDialog() {
        _isDeleteDialogOpen.value = false
        _selectedCartItem.value = null
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }


    fun removeFromCart(cartItemId: Long) {
        _cartItems.update { currentCart ->
            val updatedCart = currentCart.filter { it.id != cartItemId }
                .toList() // id, 이름, 온도, 사이즈 옵션 다 같으면 삭제
            updatedCart
        }
        closeDeleteDialog()
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        _cartItems.update { currentCart ->
            currentCart.map { cartItem ->
                if (cartItem.id == cartItemId) {
                    cartItem.copy(quantity = newQuantity) // 새로운 객체 반환
                } else {
                    cartItem // 불필요한 참조를 방지하기 위해 copy()
                }
            }
        }
    }

}