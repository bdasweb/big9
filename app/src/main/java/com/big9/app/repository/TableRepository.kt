package com.big9.app.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.big9.app.data.model.allReport.WalletLedgerData
import com.big9.app.paging.WalletLedgerRemoteMediator
import com.big9.app.utils.table.TableDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TableRepository @Inject constructor(
    private val tableDao: TableDao,
    val walletLedgerRemoteMediator: WalletLedgerRemoteMediator

) {
    /*fun getDataPaged(): PagingSource<Int, DataEntity> {
        return tableDao.getData()
    }*/
    /*@OptIn(ExperimentalPagingApi::class)
    fun getDataPaged(): Pager<Int, DataEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { tableDao.getData() },
            remoteMediator = null // You can use RemoteMediator if data is loaded from the network
        )
    }*/
    /*fun getDataPaged(): PagingSource<Int, DataEntity> {
        // Assuming tableDao.getData() returns a PagingSource<Int, DataEntity>
        return tableDao.getData()
    }*/
    suspend fun insertData(data: WalletLedgerData) {
        tableDao.insertData(data)
    }

    /*suspend fun deleteAllData(): Int {
        return tableDao.deleteAllData()
    }





    suspend fun getDataInRange(startIndex: Int, endIndex: Int): List<DataEntity?>? {
        return withContext(Dispatchers.IO) {
            tableDao.getDataInRange(startIndex, endIndex)
        }
    }
*/
    @OptIn(ExperimentalPagingApi::class)
    fun getAllWalletLedgerData(): Flow<PagingData<WalletLedgerData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = walletLedgerRemoteMediator
        ) {
            tableDao.getAllWalletLedgerData()
        }.flow
    }
}