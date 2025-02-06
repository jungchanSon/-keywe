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
    val description: String,
    val imageURL: String
)

data class CartItem(
    val id: Int,
    val menuId: Int,
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
class MenuViewModel @Inject constructor() : ViewModel() {

    //    private val _menuItems = MutableStateFlow<List<MenuData>>(emptyList())
    private val _menuItems = MutableStateFlow(
        listOf(
            MenuData(
                1,
                "아메리카노",
                "커피+물",
                2000,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                2,
                "카페라떼",
                "커피+우유",
                3000,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                3,
                "카푸치노",
                "커피+거품 우유",
                3500,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                4,
                "카페모카",
                "커피+초콜릿+우유 + 우유",
                4000,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                5,
                "에스프레소",
                "진한 커피 샷",
                2500,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                6,
                "바닐라라떼",
                "커피+우유+바닐라시럽",
                3500,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
            MenuData(
                7,
                "카라멜마끼아또",
                "커피+거품 우유+카라멜",
                4000,
                "에스프레소에 물을 혼합한 커피 SSAFYCOFFEE의 부드럽고 풍부한 바디감을 느낄 수 있는 SSAFYCOFFEE 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
            ),
        )
    )

    private val extraOptions = listOf(
        OptionData("샷 추가", 500),
        OptionData("시럽 추가", 300),
        OptionData("바닐라 시럽 추가", 300),
        OptionData("휘핑 추가", 700),
//        OptionData("샷 추가1", 500),
//        OptionData("시럽 추가1", 300),
//        OptionData("바닐라 시럽 추가1", 300),
//        OptionData("휘핑 추가1", 700),
//        OptionData("샷 추가2", 500),
//        OptionData("시럽 추가2", 300),
//        OptionData("바닐라 시럽 추가2", 300),
//        OptionData("휘핑 추가2", 700),
//        OptionData("샷 추가3", 500),
//        OptionData("시럽 추가3", 300),
//        OptionData("바닐라 시럽 추가3", 300),
//        OptionData("휘핑 추가3", 700),
    )

    val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

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

    fun getExtraOptions(): List<OptionData> = extraOptions

    fun addToCart(
        menuId: Int,
        size: String,
        temperature: String,
        extraOptions: Map<String, Int>,
        totalPrice: Int
    ) {
        val menuData = getMenuDataById(menuId) ?: return
        val sizePrice = sizePriceMap[size] ?: 0

        println("장바구니 추가됨: $menuId, $size, $temperature, $extraOptions, 총 가격=$totalPrice")

        _cartItems.update { currentCart ->
            val sortedExtraOptions = extraOptions.toSortedMap()
            val existingItemIndex = currentCart.indexOfFirst {
                it.name == menuData.name &&
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
                        menuId = menuData.id,
                        name = menuData.name,
                        price = totalPrice,
                        quantity = 1,
                        imageURL = menuData.imageURL,
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
        cartItemId: Int,
        cartItemMenuId: Int,
        size: String,
        temperature: String,
        extraOptions: Map<String, Int>
    ) {
        _cartItems.update { currentCart ->
            val menuPrice = getMenuDataById(cartItemMenuId)?.price ?: 0
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

    fun clearCart() {
        _cartItems.value = emptyList()
    }


    fun removeFromCart(cartItemId: Int) {
        _cartItems.update { currentCart ->
            val updatedCart = currentCart.filter { it.id != cartItemId }.toList() // id, 이름, 온도, 사이즈 옵션 다 같으면 삭제
            updatedCart
        }
        closeDeleteDialog()
    }

    fun updateCartQuantity(cartItemId: Int, newQuantity: Int) {
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
