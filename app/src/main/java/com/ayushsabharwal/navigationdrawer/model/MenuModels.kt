package com.ayushsabharwal.navigationdrawer.model

data class NavigationResponse(
    val result: Result
)

data class Result(
    val title: String,
    val user_photo: String,
    val menus: List<MenuItemModel>
)

data class MenuItemModel(
    val type: Int, val label: String, val icon: String?
)