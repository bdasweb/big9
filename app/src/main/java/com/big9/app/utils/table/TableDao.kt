package com.big9.app.utils.table

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.big9.app.data.model.allReport.WalletLedgerData

@Dao
interface TableDao {
   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: WalletLedgerData)*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<WalletLedgerData>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: WalletLedgerData)

    @Query("DELETE FROM WalletLedgerdata_table")
    suspend fun clearTable()

    @Query("SELECT * FROM WalletLedgerdata_table")
    fun getAllWalletLedgerData(): PagingSource<Int, WalletLedgerData>


   /* @Query("SELECT * FROM data_table ORDER BY id ASC")
    fun getData(): PagingSource<Int, DataEntity>*/

   /* @Query("SELECT * FROM data_table")
    fun getData(): PagingSource<Int, DataEntity>


    @Query("DELETE FROM data_table")
    suspend fun deleteAllData(): Int

    @Query("SELECT * FROM data_table WHERE id BETWEEN :startIndex AND :endIndex")
    fun getDataInRange(startIndex: Int, endIndex: Int): List<DataEntity?>?*/
}