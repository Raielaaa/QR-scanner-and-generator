package com.example.qrscannerpractice2.qrdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DBViewModel(private val dao: Dao): ViewModel() {
    fun insertEntityFromVM(entity: Entity) = viewModelScope.launch {
        dao.insertEntity(entity)
    }
    fun updateEntityFromVM(entity: Entity) = viewModelScope.launch {
        dao.updateEntity(entity)
    }
    fun deleteEntityFromVM(entity: Entity) = viewModelScope.launch {
        dao.deleteEntity(entity)
    }
    val getAllEntityFromVM: LiveData<List<Entity>> = dao.getAllEntity()
}