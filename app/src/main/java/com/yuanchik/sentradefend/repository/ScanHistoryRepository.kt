package com.yuanchik.sentradefend.repository

import com.yuanchik.sentradefend.dao.ScanResultDao
import com.yuanchik.sentradefend.entity.ScanResultEntity
import kotlinx.coroutines.flow.Flow

class ScanHistoryRepository(private val dao: ScanResultDao) {

    val allResults: Flow<List<ScanResultEntity>> = dao.getAll()

    suspend fun saveResult(scanResult: ScanResultEntity) {
        dao.insert(scanResult)
    }
}
