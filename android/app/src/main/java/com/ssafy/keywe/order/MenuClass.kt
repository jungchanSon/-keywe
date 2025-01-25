package com.ssafy.keywe.order

data class Menu(
    val id: Int,
    val data: MenuData
)

data class MenuData(
    val name: String,
    val imageURL: String,
    val price: Int
)