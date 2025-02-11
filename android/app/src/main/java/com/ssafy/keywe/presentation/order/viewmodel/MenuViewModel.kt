package com.ssafy.keywe.presentation.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.domain.order.CategoryModel
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

data class MenuData(
    val id: Int,
    val category: String,
    val name: String,
    val recipe: String,
    val price: Int,
    val description: String,
    val imageURL: String
)

data class MenuCategory(
    val id: Int,
    val name: String
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
class OrderViewModel @Inject constructor(private val repository: OrderRepository) : ViewModel() {

    //    private val _menuItems = MutableStateFlow<List<MenuData>>(emptyList())
    private val _menuItems = MutableStateFlow(
        listOf(
            // 커피
            MenuData(
                1,
                "커피",
                "아메리카노",
                "커피+물",
                2000,
                "에스프레소에 물을 혼합한 커피 COFFEE 부드럽고 풍부한 바디감을 느낄 수 있는 대표 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/americano.png?raw=true"
            ),
            MenuData(
                2,
                "커피",
                "카페라떼",
                "커피+우유",
                3000,
                "에스프레소에 우유를 더한 부드러운 라떼",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/cafe_latte.png?raw=true"
            ),
            MenuData(
                3,
                "커피",
                "카페모카",
                "커피+초콜릿+우유",
                4000,
                "초콜릿과 에스프레소가 어우러진 달콤한 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/cafe_mocha.png?raw=true"
            ),
            MenuData(
                4,
                "커피",
                "에스프레소",
                "진한 커피 샷",
                2500,
                "고농축의 진한 커피 한 잔",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/espresso.png?raw=true"
            ),
            MenuData(
                5,
                "커피",
                "바닐라 라떼",
                "커피+우유+바닐라 시럽",
                3500,
                "달콤한 바닐라 향이 더해진 부드러운 라떼",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/vanilla_latte.png?raw=true"
            ),

            // 차
            MenuData(
                6,
                "차",
                "캐모마일 티",
                "캐모마일 차",
                3500,
                "부드럽고 향긋한 캐모마일 티",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/chamomile_tea.png?raw=true"
            ),
            MenuData(
                7,
                "차",
                "얼그레이 티",
                "얼그레이 차",
                3500,
                "홍차의 깊은 풍미와 베르가못 향이 어우러진 얼그레이 티",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/earl_grey_tea.png?raw=true"
            ),
            MenuData(
                8,
                "차",
                "그린티 라떼",
                "녹차+우유",
                4000,
                "진한 녹차와 우유가 어우러진 부드러운 라떼",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/greentea_latte.png?raw=true"
            ),
            MenuData(
                9,
                "차",
                "자스민 티",
                "자스민 차",
                3500,
                "향긋한 자스민 꽃 향이 가득한 차",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/jasmine_tea.png?raw=true"
            ),
            MenuData(
                10,
                "차",
                "페퍼민트 티",
                "페퍼민트 차",
                3500,
                "시원하고 개운한 페퍼민트 티",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/peppermint_tea.png?raw=true"
            ),

            // 에이드
            MenuData(
                11,
                "에이드",
                "자몽 에이드",
                "자몽+탄산수",
                4500,
                "상큼한 자몽과 탄산이 어우러진 시원한 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/grapefruit_ade.png?raw=true"
            ),
            MenuData(
                12,
                "에이드",
                "레몬 에이드",
                "레몬+탄산수",
                4500,
                "상큼한 레몬과 탄산이 어우러진 청량한 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/lemon_ade.png?raw=true"
            ),

            // 스무디
            MenuData(
                13,
                "스무디",
                "아이스 초콜릿",
                "초콜릿+우유+얼음",
                4000,
                "달콤한 초콜릿과 우유가 어우러진 시원한 음료",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/iced_chocolate.png?raw=true"
            ),
            MenuData(
                14,
                "스무디",
                "망고 스무디",
                "망고+우유+얼음",
                5000,
                "달콤하고 진한 망고가 가득한 스무디",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/mango_smoothie.png?raw=true"
            ),
            MenuData(
                15,
                "스무디",
                "요거트 스무디",
                "요거트+우유+얼음",
                5000,
                "진한 요거트 맛이 매력적인 스무디",
                "https://github.com/Bheinarl/Android-Studio-Study/blob/master/images/yogurt_smoothie.png?raw=true"
            )
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

    //    private val categories = listOf(
//        MenuCategory(1, "전체"),
//        MenuCategory(2, "커피"),
//        MenuCategory(3, "에이드"),
//        MenuCategory(4, "스무디"),
//        MenuCategory(5, "차"),
//    )

    suspend fun getCategory(): ResponseResult<List<CategoryModel>> {
        return repository.getCategory()
    }

    private val _categories = MutableStateFlow<List<CategoryModel>>(listOf(CategoryModel(0, "전체")))
    val categories: StateFlow<List<CategoryModel>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            when (val result = repository.getCategory()) {
                is ResponseResult.Success -> {
                    val categoriesWithAll = listOf(CategoryModel(0, "전체")) + result.data
                    _categories.value = categoriesWithAll
                }
                is ResponseResult.ServerError -> {
                    println("서버 에러: ${result.status}")  // 서버 오류 로그
                }
                is ResponseResult.Exception -> {
                    println("예외 발생: ${result.e.message}")  // 예외 로그
                }
            }
        }
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    val filteredMenuItems: StateFlow<List<MenuData>> = selectedCategory
        .map { category ->
            if (category == "전체") { _menuItems.value }
            else { _menuItems.value.filter { it.category == category } }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

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

    fun getExtraOptions(): List<OptionData> = extraOptions

    fun getMenuCategories(): List<CategoryModel> = _categories.value

    fun addToCart(
        menuId: Int,
        size: String,
        temperature: String,
        extraOptions: Map<String, Int>,
        totalPrice: Int
    ) {
        val menuData = getMenuDataById(menuId) ?: return

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
