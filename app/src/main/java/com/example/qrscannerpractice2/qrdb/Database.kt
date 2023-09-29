package com.example.qrscannerpractice2.qrdb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [Entity::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun dao(): Dao
    companion object {
        @Volatile
        var INSTANCE: Database? = null
        fun getInstance(context: Context): Database = synchronized(this@Companion) {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "qr_local_database"
            ).build()
        }
    }
}