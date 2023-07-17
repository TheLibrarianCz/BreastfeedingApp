package com.example.kojeniapp.database.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(indices = [Index("id"), Index("date")])
data class Feeding(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Contains date in format YYYY-MM-DD
     */
    val date: LocalDate,

    /**
     * Contains time in format HH:MM:SS
     */
    val timestamp: LocalTime,

    /**
     * Contains LEFT or RIGHT constants from BreastType
     */
    val breast: Int = 0,

    /**
     * Contains flags VIGANTOL, ESPUMISAN, PROBIOTICS from AdditionalConstants
     */
    val additions: Int = 0
)
