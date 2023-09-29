package com.example.qrscannerpractice2.qrdb

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "qr_main_table")
data class Entity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("qr_id")
    val id: Int,
    @ColumnInfo("qr_author")
    val bookAuthor: String,
    @ColumnInfo("qr_book")
    val bookName: String
)
