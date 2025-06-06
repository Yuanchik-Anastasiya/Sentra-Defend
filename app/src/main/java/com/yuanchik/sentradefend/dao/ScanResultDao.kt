package com.yuanchik.sentradefend.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yuanchik.sentradefend.entity.ScanResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scanResult: ScanResultEntity)

    @Query("SELECT * FROM scan_results ORDER BY id DESC")
    fun getAll(): Flow<List<ScanResultEntity>>

    @Query("SELECT * FROM scan_results ORDER BY id DESC")
    fun getPagedResults(): PagingSource<Int, ScanResultEntity>

    @Query("DELETE FROM scan_results")
    suspend fun clearAll()

}