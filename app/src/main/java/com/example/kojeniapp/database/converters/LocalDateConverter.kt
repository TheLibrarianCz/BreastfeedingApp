package com.example.kojeniapp.database.converters

import androidx.room.TypeConverter
import java.time.LocalDate

object LocalDateConverter {

    @JvmStatic
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @JvmStatic
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()
}
