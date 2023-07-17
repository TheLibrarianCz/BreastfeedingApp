package com.example.kojeniapp.database.models

import androidx.room.ColumnInfo

data class StatisticsSubmodel(
    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "difference")
    var difference: Float
)
