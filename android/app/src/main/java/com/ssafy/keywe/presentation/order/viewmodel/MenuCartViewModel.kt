package com.ssafy.keywe.presentation.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.OrderMenuItemModel
import com.ssafy.keywe.domain.order.OrderModel
import com.ssafy.keywe.domain.order.OrderOptionItemModel
import com.ssafy.keywe.domain.order.OrderRepository
import com.ssafy.keywe.domain.order.OrderResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuCartViewModel @Inject constructor(private val repository: OrderRepository) : ViewModel() {

    data class CartItem(
        val id: Long,
        val menuId: Long,
        val name: String,
        val price: Int,
        val quantity: Int,
        val image: String?,
        val size: String,
        val temperature: String,
        val extraOptions: Map<Long, Pair<String, Int>>
    )

    val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _isDeleteDialogOpen = MutableStateFlow(false)
    val isDeleteDialogOpen: StateFlow<Boolean> = _isDeleteDialogOpen.asStateFlow()

    private val _isCompleteOrder = MutableStateFlow(false)
    val isCompleteOrder: StateFlow<Boolean> = _isCompleteOrder.asStateFlow()

    private val _isCompleteOrderGoHome = MutableStateFlow(false)
    val isCompleteOrderGoHome: StateFlow<Boolean> = _isCompleteOrderGoHome.asStateFlow()

    private val _selectedCartItem = MutableStateFlow<CartItem?>(null)
    val selectedCartItem: StateFlow<CartItem?> = _selectedCartItem.asStateFlow()

    private val _selectedDetailMenu = MutableStateFlow<MenuDetailModel?>(null)
    val selectedDetailMenu: StateFlow<MenuDetailModel?> = _selectedDetailMenu.asStateFlow()

    fun fetchMenuDetailById(id: Long): MenuDetailModel? {
        Timber.tag("Fetch Menu").d(":$id")

        viewModelScope.launch {
            when (val result = repository.getDetailMenu(id)) {
                is ResponseResult.Success -> {
                    val menuDetail = result.data
                    _selectedDetailMenu.value = menuDetail
                    println("getDetailMenu 결과: $menuDetail")
                    menuDetail.options.forEach { option ->
                        Timber.tag("옵션 정보")
                            .d("옵션명: ${option.optionName}, 옵션 ID: ${option.optionId}")
                    }

                }

                is ResponseResult.ServerError -> {
                    println("서버 에러: ${result.status}")  // 서버 오류 로그
                }

                is ResponseResult.Exception -> {
                    println("예외 발생: ${result.e.message}")  // 예외 로그
                }
            }
        }
        Timber.tag("selectDetailMenu 메뉴:").d("${selectedDetailMenu.value}")
        return selectedDetailMenu.value
    }

    fun addToCart(
        menuId: Long,
        size: String,
        temperature: String,
        selectedOptions: Map<Long, Int>,
        totalPrice: Int
    ) {
        viewModelScope.launch {
            _selectedDetailMenu.value = null

            fetchMenuDetailById(menuId)

            var retryCount = 0
            while (selectedDetailMenu.value == null && retryCount < 6) {
                delay(500)
                retryCount++
            }

            val menuData = selectedDetailMenu.value
            if (menuData == null) {
                Timber.tag("addToCart").e("메뉴 데이터가 null입니다. menuId: $menuId")
                return@launch
            }

            val sizeOptionId = menuData.options
                .flatMap { it.optionsValueGroup }
                .find { it.optionValue == size }?.optionValueId

            val temperatureOptionId = menuData.options
                .flatMap { it.optionsValueGroup }
                .find { it.optionValue == temperature }?.optionValueId

            Timber.tag("addToCart")
                .d("✅ 찾은 $size 의 sizeOptionId: $sizeOptionId, $temperature 의 temperatureOptionId: $temperatureOptionId")

            val updatedSelectedOptions = selectedOptions.toMutableMap()
            sizeOptionId?.let { updatedSelectedOptions[it] = 1 }
            temperatureOptionId?.let { updatedSelectedOptions[it] = 1 }

            val cartExtraOptions = updatedSelectedOptions.mapValues { (optionId, count) ->
                val optionName = menuData.options.flatMap { it.optionsValueGroup }
                    .find { it.optionValueId == optionId }?.optionValue ?: "Unknown"

                optionName to count
            }

            val currentCart = _cartItems.value.toMutableList()

            val existingItemIndex = currentCart.indexOfFirst {
                it.name == menuData.menuName &&
                        it.size == size &&
                        it.temperature == temperature &&
                        it.extraOptions == cartExtraOptions
            }

            if (existingItemIndex != -1) {
                currentCart[existingItemIndex] = currentCart[existingItemIndex].copy(
                    quantity = currentCart[existingItemIndex].quantity + 1
                )
                Timber.tag("addToCart").d("✅ 기존 아이템 수량 증가: ${currentCart[existingItemIndex]}")
            } else {
                val newId = (currentCart.maxOfOrNull { it.id } ?: 0) + 1

                currentCart.add(
                    CartItem(
                        id = newId,
                        menuId = menuData.menuId,
                        name = menuData.menuName,
                        price = totalPrice,
                        quantity = 1,
                        image = menuData.image,
                        size = size,
                        temperature = temperature,
                        extraOptions = cartExtraOptions
                    )
                )
            }

            // ✅ 업데이트 반영
            _cartItems.value = currentCart.toList()
            Timber.tag("addToCart").d("🛒 최종 장바구니 상태: ${_cartItems.value}")
        }
    }

    fun updateCartItem(
        cartItemId: Long,
        cartItemMenuId: Long,
        size: String,
        temperature: String,
        extraOptions: Map<Long, Int>
    ) {
        Timber.tag("updateCartItem").d("updateCartItem 호출됨: id=$cartItemId, menuId=$cartItemMenuId") // ✅ 함수 호출 로그 추가

        viewModelScope.launch {
            Timber.tag("updateCartItem").d("✅ fetchMenuDetailById 호출 전")
            fetchMenuDetailById(cartItemMenuId)
            Timber.tag("updateCartItem").d("✅ fetchMenuDetailById 호출 후")

            var retryCount = 0
            while (selectedDetailMenu.value == null && retryCount < 6) {
                Timber.tag("updateCartItem").d("⏳ 메뉴 데이터 로딩 중... (retryCount: $retryCount)")
                delay(500)
                retryCount++
            }

            if (selectedDetailMenu.value == null) {
                Timber.tag("updateCartItem").e("❌ 메뉴 상세 정보를 가져오지 못했습니다. menuId: $cartItemMenuId")
                return@launch
            }

            Timber.tag("updateCartItem").d("✅ 메뉴 상세 정보 가져옴: ${selectedDetailMenu.value}")

            val menuDetail = selectedDetailMenu.value ?: return@launch

            val menuPrice = menuDetail.menuPrice
            val sizePrice = sizePriceMap[size] ?: 0

            val filteredExtraOptions = extraOptions.mapValues { (optionValueId, count) ->
                val optionModel = menuDetail.options.find { option ->
                    option.optionsValueGroup.any { it.optionValueId == optionValueId }
                }

                val optionType = optionModel?.optionType ?: "Unknown"
                val optionValue = optionModel?.optionsValueGroup?.find { it.optionValueId == optionValueId }?.optionValue ?: "Unknown"

                // ✅ "Extra" 옵션만 표시 (Common 옵션 등 제외)
                if (optionType == "Extra") {
                    optionValue to count
                } else {
                    "" to 0 // Common 옵션 등은 표시하지 않음
                }
            }

            val extraOptionPrice = filteredExtraOptions.entries.sumOf { (_, pair) ->
                val (optionValue, count) = pair
                val optionModel = menuDetail.options.find { option ->
                    option.optionsValueGroup.any { it.optionValue == optionValue }
                }

                val optionPrice = optionModel?.optionPrice ?: 0
                optionPrice * count
            }

            val newTotalPrice = menuPrice + sizePrice + extraOptionPrice

            _cartItems.update { currentCart ->
                Timber.tag("Cart Update").d("기존 장바구니: $currentCart")
                val existingItemIndex = currentCart.indexOfFirst {
                    it.menuId == cartItemMenuId &&
                            it.size == size &&
                            it.temperature == temperature &&
                            it.extraOptions == extraOptions &&
                            it.id != cartItemId
                }

                val updatedCart = currentCart.toMutableList()
                Timber.tag("Cart Update").d("새 장바구니: $updatedCart")

                if (existingItemIndex != -1) {
                    updatedCart[existingItemIndex] = updatedCart[existingItemIndex].copy(
                        quantity = updatedCart[existingItemIndex].quantity + 1
                    )
                    updatedCart.removeIf { it.id == cartItemId }
                } else {
                    updatedCart.replaceAll { cartItem ->
                        if (cartItem.id == cartItemId) {
                            cartItem.copy(
                                size = size,
                                temperature = temperature,
                                extraOptions = filteredExtraOptions,
                                price = newTotalPrice
                            )
                        } else {
                            cartItem
                        }
                    }
                }

                updatedCart
            }
        }
    }

//    fun getOptionTypeByOptionValueId(optionValueId: Long): String {
//        var retryCount = 0
//        var optionType: String? = null
//
//        while (retryCount < 3) {
//            optionType = selectedDetailMenu.value?.options
//                ?.find { optionModel ->
//                    optionModel.optionsValueGroup.any { it.optionValueId == optionValueId }
//                }?.optionType
//
//            if (optionType != null) break
//            retryCount++
//        }
//
//        return optionType ?: "Unknown"
//    }

//    private fun getOptionValueById(optionValueId: Long): String {
//        return selectedDetailMenu.value?.options
//            ?.flatMap { it.optionsValueGroup }
//            ?.find { it.optionValueId == optionValueId }
//            ?.optionValue ?: "Unknown"
//    }

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
                .toList()
            updatedCart
        }
        closeDeleteDialog()
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        _cartItems.update { currentCart ->
            currentCart.map { cartItem ->
                if (cartItem.id == cartItemId) {
                    cartItem.copy(quantity = newQuantity)
                } else {
                    cartItem
                }
            }
        }
    }

    private val _orderResponse = MutableStateFlow<OrderResponseModel?> (null)
    val orderResponse: StateFlow<OrderResponseModel?> = _orderResponse

    fun postOrder(orderModel: OrderModel, onResult: (Result<OrderResponseModel>) -> Unit) {
        viewModelScope.launch{
            Timber.tag("postOrder OrderModel 확인").d("$orderModel")
            when (val result = repository.postOrder(orderModel)) {
                is ResponseResult.Success -> {
                    val responseBody = result.data
                    Timber.tag("postOrder").d("✅ 원본 응답 데이터: $responseBody")

                    // 📌 서버 응답이 단순 숫자라면 직접 OrderResponseModel로 변환
                    val parsedResponse = try {
                        OrderResponseModel(orderId = responseBody.orderId)
                    } catch (e: Exception) {
                        Timber.tag("postOrder").e("🚨 응답 데이터 파싱 실패: ${e.message}")
                        null
                    }

                    if (parsedResponse != null) {
                        _orderResponse.value = parsedResponse
                        Timber.tag("postOrder").d("✅ 변환된 응답 데이터: $parsedResponse")
                        _isCompleteOrder.value = true
                        onResult(Result.success(parsedResponse))
                    } else {
                        onResult(Result.failure(Exception("파싱 실패")))
                    }
                }

                is ResponseResult.ServerError -> {
                    Timber.tag("postOrder 실패: 서버에러").d("${result.status}")
                }

                is ResponseResult.Exception -> {
                    Timber.tag("postOrder 실패: 예외").d("${result.e.message}")
                }
            }
        }
    }

    fun createOrderModel(phoneNumber: String): OrderModel {
        val menuList = _cartItems.value.map { cartItem ->
            OrderMenuItemModel(
                menuId = cartItem.menuId,
                menuCount = cartItem.quantity,
                optionList = cartItem.extraOptions.map { (optionValueId, pair) ->  // ✅ 옵션 ID 기반
                    OrderOptionItemModel(
                        optionValueId = optionValueId, // ✅ 옵션 ID 그대로 사용
                        optionCount = pair.second  // ✅ 옵션 개수 사용
                    )
                }
            )
        }

        Timber.tag("createOrderModel").d("생성된 주문 모델: $menuList")

        return OrderModel(
            phoneNumber = phoneNumber,
            menuList = menuList
        )
    }


    fun closeCompleteOrderDialog() {
        _isCompleteOrder.value = false
        _isCompleteOrderGoHome.value = false
        clearCart()
    }

}