package com.example.kojeniapp.database.converters

import androidx.room.TypeConverter
import java.time.LocalTime

object LocalTimeConverter {

    @JvmStatic
    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? = value?.let { LocalTime.parse(it) }

    @JvmStatic
    @TypeConverter
    fun fromLocalTime(date: LocalTime?): String? = date?.toString()
}
