package com.accountmanager.smartledger.presentation.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*data class Account(
    @SerializedName("ActName") val actName: String? = null,
    @SerializedName("actid") val actId: Int ? = null,
    @SerializedName("alternateName") val alternateName: String? = null
)*/


@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey val actid: Int? = null,
    val ActName: String? = null,
    val balance: Double? = null,
    val alternateName: String? = null
)
