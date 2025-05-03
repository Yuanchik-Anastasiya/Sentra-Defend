package com.yuanchik.sentradefend.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yuanchik.sentradefend.dao.ScanResultDao
import com.yuanchik.sentradefend.entity.ScanResultEntity

@Database(entities = [ScanResultEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanResultDao(): ScanResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "scan_history_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

