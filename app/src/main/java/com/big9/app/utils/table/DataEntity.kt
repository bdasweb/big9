package com.big9.app.utils.table
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val responseId: String? = "",
    val price: String? = "",
    val reportDate: String? = "",
    val reporyStatus: String? = "",
    val reporyStatusFlag: Int? = 0,
    val desc: String? = "",
    val imageInt: Int? = 0,
    val image1: Int? = 1,
    val price2: String? = "",
    val proce1TextColor: Int = 0,
    val isMiniStatement: Boolean = false,
    val isClickAble: Boolean = false,
    val miniStatementValue: String? = ""
)