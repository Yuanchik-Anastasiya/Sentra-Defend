package com.yuanchik.sentradefend.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yuanchik.sentradefend.dao.ScanResultDao
import com.yuanchik.sentradefend.entity.ScanResultEntity
import kotlinx.coroutines.flow.Flow

class ScanHistoryRepository(private val dao: ScanResultDao) {

    fun getPagedResults(): Flow<PagingData<ScanResultEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getPagedResults() }
        ).flow
    }

    suspend fun saveResult(scanResult: ScanResultEntity) {
        dao.insert(scanResult)
    }
}

