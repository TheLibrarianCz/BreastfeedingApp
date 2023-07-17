package com.example.kojeniapp.database.models

import androidx.room.ColumnInfo

data class StatisticsModel(
    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "average")
    val average: Float,

    @ColumnInfo(name = "maximum")
    val maximum: Float,

    @ColumnInfo(name = "total")
    val total: Int
)
