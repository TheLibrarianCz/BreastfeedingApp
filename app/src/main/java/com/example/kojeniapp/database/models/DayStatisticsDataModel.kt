package com.example.kojeniapp.database.models

import java.time.LocalTime

data class DayStatisticsDataModel(
    val date: String,
    val feedingCount: Int,
    val averageTimeBetweenFeedings: LocalTime,
    val maxTimeBetweenFeedings: LocalTime
)
