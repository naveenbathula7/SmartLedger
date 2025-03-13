package com.accountmanager.smartledger.domain

import com.accountmanager.smartledger.data.AccountDao
import com.accountmanager.smartledger.presentation.model.Account
import com.accountmanager.smartledger.network.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val apiService: ApiService, private val accountDao: AccountDao
) {
    suspend fun fetchAccounts() {
        val response = apiService.getAccounts()
        val gson = Gson()

        val listType = object : TypeToken<List<Account>>() {}.type
        val accountList: List<Account> = gson.fromJson(response, listType)
        accountDao.saveAccounts(accountList)
    }

    fun getAccounts(): Flow<List<Account>> = accountDao.getAllAccounts()

    suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }

    suspend fun updateAccountName(accountId: Int, newName: String) {
        accountDao.updateAccountName(accountId, newName)
    }
}

