package com.accountmanager.smartledger.data

import androidx.room.*
import com.accountmanager.smartledger.presentation.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAccounts(accounts: List<Account>)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("UPDATE accounts SET ActName = :newName WHERE actId = :accountId")
    suspend fun updateAccountName(accountId: Int, newName: String)

    @Delete
    suspend fun deleteAccount(account: Account)
}
