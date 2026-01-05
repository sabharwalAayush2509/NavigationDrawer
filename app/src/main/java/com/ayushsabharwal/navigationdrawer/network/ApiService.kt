package com.ayushsabharwal.navigationdrawer.network

import com.ayushsabharwal.navigationdrawer.model.NavigationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("navigation")
    suspend fun getNavigation(
        @Query("restApi") restApi: String = "Sesapi",
        @Query("sesapi_platform") platform: Int = 1,
        @Query("auth_token") token: String
    ): NavigationResponse
}