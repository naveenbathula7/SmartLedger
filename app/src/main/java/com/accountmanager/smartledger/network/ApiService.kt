package com.accountmanager.smartledger.network

import com.accountmanager.smartledger.presentation.model.Account
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class ApiResponse(
    @SerializedName("data") val data: List<Account>
)

interface ApiService {
    @GET("Fillaccounts/nadc/2024-2025")
    suspend fun getAccounts(): String
}

