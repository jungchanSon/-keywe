package com.ssafy.keywe.presentation.order

data class Menu(
    val id: Int,
    val data: MenuData,
)

data class MenuData(
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