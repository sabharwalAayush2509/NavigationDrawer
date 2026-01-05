package com.ayushsabharwal.navigationdrawer.model

data class NavigationResponse(
    val result: Result
)

data class Result(
    val menus: List<MenuItemModel>
)

data class MenuItemModel(
    val type: Int, val label: String, val icon: String?
)