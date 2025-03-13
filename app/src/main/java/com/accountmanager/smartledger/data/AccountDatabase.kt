package com.accountmanager.smartledger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.accountmanager.smartledger.presentation.model.Account

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}
