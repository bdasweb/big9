package com.big9.app.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.big9.app.data.model.allReport.WalletLedgerData
import com.big9.app.utils.table.TableDatabase
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class WalletLedgerRemoteMediator @Inject constructor(
    private val database: TableDatabase
) : RemoteMediator<Int, WalletLedgerData>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WalletLedgerData>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.id ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // Simulate or replace this with your actual local data source
            val localData = getLocalData()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Clear the table when refreshing
                    database.tableDao().clearTable()
                }

                // Insert the new data into the database
                database.tableDao().insertAll(localData)
            }

            return MediatorResult.Success(endOfPaginationReached = localData.isEmpty())
        } catch (exception: IOException) {
            // Network error (simulated or not needed)
            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (exception: Exception) {
            // Other exceptions
            return MediatorResult.Error(exception)
        }
    }

    // Simulate or replace this with your actual local data source
    private fun getLocalData(): List<WalletLedgerData> {
        // Simulate local data (replace with your logic)
        return listOf(
            WalletLedgerData(id = 1, /* other properties */),
            WalletLedgerData(id = 2, /* other properties */),
            // Add more items as needed
        )
    }
}