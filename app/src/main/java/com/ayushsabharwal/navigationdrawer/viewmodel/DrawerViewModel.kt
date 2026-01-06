package com.ayushsabharwal.navigationdrawer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsabharwal.navigationdrawer.model.NavigationResponse
import com.ayushsabharwal.navigationdrawer.network.ApiClient
import kotlinx.coroutines.launch

class DrawerViewModel : ViewModel() {

    private val _navigationResponse = MutableLiveData<NavigationResponse>()
    val navigationResponse: LiveData<NavigationResponse> = _navigationResponse

    fun loadMenus() {
        viewModelScope.launch {
            try {
                val response = ApiClient.api().getNavigation(token = "B179086bb56c32731633335762")
                _navigationResponse.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}