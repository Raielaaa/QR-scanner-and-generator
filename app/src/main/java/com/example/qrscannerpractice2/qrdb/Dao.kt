package com.example.qrscannerpractice2.qrdb

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@androidx.room.Dao
interface Dao {
    @Insert
    suspend fun insertEntity(entity: Entity)
    @Update
    suspend fun updateEntity(entity: Entity)
    @Delete
    suspend fun deleteEntity(entity: Entity)
    @Query("SELECT * FROM qr_main_table")
    fun getAllEntity(): LiveData<List<Entity>>
}