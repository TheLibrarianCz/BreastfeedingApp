package com.example.kojeniapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kojeniapp.database.converters.LocalDateConverter
import com.example.kojeniapp.database.converters.LocalTimeConverter
import com.example.kojeniapp.database.dao.FeedingDao
import com.example.kojeniapp.database.models.Feeding

@Database(
    entities = [
        Feeding::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        LocalDateConverter::class,
        LocalTimeConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedingDao(): FeedingDao
}
