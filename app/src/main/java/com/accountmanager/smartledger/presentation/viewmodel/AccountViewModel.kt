package com.accountmanager.smartledger.presentation.viewmodel

import NetworkUtils
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.accountmanager.smartledger.presentation.model.Account
import com.accountmanager.smartledger.domain.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: AccountRepository, applicationContext: Application) : AndroidViewModel(applicationContext) {

    private val _isDataFetched = MutableStateFlow(false)
    val isDataFetched = _isDataFetched.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun checkInternetAndFetchAccounts(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val isNetworkAvailable = NetworkUtils.isNetworkAvailable(context)

            if (isNetworkAvailable) {
                fetchAccountsFromApi()
            } else {
                fetchAccountsFromLocal()
            }
        }
    }

    private suspend fun fetchAccountsFromApi() {
        repository.fetchAccounts()
        repository.getAccounts().collect { accountList ->
            _accounts.value = accountList
            _isDataFetched.value = true
            _isLoading.value = false
        }
    }

    private fun fetchAccountsFromLocal() {
        viewModelScope.launch {
            repository.getAccounts().collect { accountList ->
                _accounts.value = accountList
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
            fetchAccountsFromLocal()
        }
    }

    fun updateAccountName(accountId: Int, newName: String) {
        viewModelScope.launch {
            repository.updateAccountName(accountId, newName)
            fetchAccountsFromLocal()
        }
    }
}