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