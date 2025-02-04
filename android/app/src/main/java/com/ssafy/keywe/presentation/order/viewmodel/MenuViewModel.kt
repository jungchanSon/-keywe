package com.ssafy.keywe.presentation.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MenuData(
    val id: Int,
    val name: String,
    val recipe: String,
    val price: Int,
    val imageURL: String
)

data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    val quantity: Int,
    val imageURL: String,
    val size: String,
    val temperature: String,
    val extraOptions: Map<String, Int>
)

data class OptionData(
    val name: String,
    val price: Int,
)

@HiltViewModel
class MenuViewModel @Inject constructor()  : ViewModel() {

    //    private val _menuItems = MutableStateFlow<List<MenuData>>(emptyList())
    private val _menuItems = MutableStateFlow(
        listOf(
            MenuData(
                1,
                "아메리카노",
                "커피+물",
                2000,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                2,
                "카페라떼",
                "커피+우유",
                3000,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                3,
                "카푸치노",
                "커피+거품 우유",
                3500,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                4,
                "카페모카",
                "커피+초콜릿+우유 + 우유",
                4000,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                5,
                "에스프레소",
                "진한 커피 샷",
                2500,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                6,
                "바닐라라떼",
                "커피+우유+바닐라시럽",
                3500,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                7,
                "카라멜마끼아또",
                "커피+거품 우유+카라멜",
                4000,
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
        )
    )
    val menuItems: StateFlow<List<MenuData>> = _menuItems.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    val cartItemCount: StateFlow<Int> = _cartItems.map { it.sumOf { item -> item.quantity } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _isDeleteDialogOpen = MutableStateFlow(false)
    val isDeleteDialogOpen: StateFlow<Boolean> = _isDeleteDialogOpen.asStateFlow()

    private val _selectedCartItem = MutableStateFlow<CartItem?>(null)
    val selectedCartItem: StateFlow<CartItem?> = _selectedCartItem.asStateFlow()

    fun openDeleteDialog(cartItem: CartItem) {
        _selectedCartItem.value = cartItem
        _isDeleteDialogOpen.value = true
    }

    fun closeDeleteDialog() {
        _isDeleteDialogOpen.value = false
        _selectedCartItem.value = null
    }

    fun getMenuDataById(id: Int): MenuData? {
        return _menuItems.value.find { it.id == id }
    }

    fun getCartMenuById(id: Int): CartItem? {
        return _cartItems.value.find { it.id == id }
    }

    fun addToCart(
        menuId: Int,
        size: String,
        temperature: String,
        extraOptions: Map<String, Int>,
        totalPrice: Int
    ) {
        val menuData = getMenuDataById(menuId) ?: return // 메뉴가 없으면 실행하지 않음
        val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)
        val sizePrice = sizePriceMap[size] ?: 0

        println("장바구니 추가됨: $menuId, $size, $temperature, $extraOptions, 총 가격=$totalPrice")

        _cartItems.update { currentCart ->
            val existingItemIndex = currentCart.indexOfFirst {
                it.name == menuData.name &&
                        it.size == size &&
                        it.temperature == temperature &&
                        it.extraOptions == extraOptions
            }

            if (existingItemIndex != -1) {
                currentCart.toMutableList().apply {
                    this[existingItemIndex] =
                        this[existingItemIndex].copy(quantity = this[existingItemIndex].quantity + 1)
                }
            } else {
                currentCart + CartItem(
                    id = currentCart.size + 1,
                    name = menuData.name,
                    price = totalPrice,
                    quantity = 1,
                    imageURL = menuData.imageURL,
                    size = size,
                    temperature = temperature,
                    extraOptions = extraOptions
                )
            }
        }
        println("현재 장바구니 상태: ${_cartItems.value}")
    }

    fun removeFromCart() {
        _selectedCartItem.value?.let { item ->
            _cartItems.update { currentCart ->
                currentCart.filter { it.id != item.id }
            }
        }
        closeDeleteDialog()
    }

    fun updateCartQuantity(itemId: Int, newQuantity: Int) {
        _cartItems.value = _cartItems.value.map { cartItem ->
            if (cartItem.id == itemId) {
                cartItem.copy(quantity = newQuantity)
            } else {
                cartItem
            }
        }
    }
}
