package com.example.qrscannerpractice2.qrdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DBViewModelFactory(private val dao: Dao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Checks if ViewModel (modelClass) is a subclass or child class of DBViewModel class
        if (modelClass.isAssignableFrom(DBViewModel::class.java)) {
            return DBViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}