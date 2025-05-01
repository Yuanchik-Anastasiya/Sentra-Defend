package com.yuanchik.sentradefend.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yuanchik.sentradefend.data.AppDatabase
import com.yuanchik.sentradefend.entity.ScanResultEntity
import com.yuanchik.sentradefend.repository.ScanResultRepository
import kotlinx.coroutines.launch

class ScanResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScanResultRepository

    val allResults: LiveData<List<ScanResultEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).scanResultDao()
        repository = ScanResultRepository(dao)
        allResults = repository.allResults.asLiveData()
    }

    fun insertScanResult(scanResult: ScanResultEntity) = viewModelScope.launch {
        repository.insert(scanResult)
    }
}
