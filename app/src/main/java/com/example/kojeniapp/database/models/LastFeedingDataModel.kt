package com.example.kojeniapp.database.models

import android.util.Log
import com.example.kojeniapp.utils.DateUtils
import com.example.kojeniapp.utils.FlagUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class LastFeedingDataModel(
    feeding: Feeding?,
    nextFeedingHourInterval: Int
) {
    private var lastFeeding: Feeding
    private val nextFeedingDate: LocalDateTime

    val timestamp: String
        get() = try {
            lastFeeding.timestamp.format(DateUtils.getTimeFormatter())
        } catch (ex: Exception) {
            Log.e("getTimestamp", "Exception message: ${ex.message}")
            "<Unknown>"
        }

    val date: String
        get() = try {
            lastFeeding.date.format(DateUtils.getDateFormatter())
        } catch (ex: Exception) {
            Log.e("getDate", "Exception message: ${ex.message}")
            "<Unknown>"
        }

    val nextFeeding: String
        get() = nextFeedingDate.toLocalTime().format(DateUtils.getTimeFormatter())

    val isNextFeedingTomorrow: Boolean
        get() = nextFeedingDate.toLocalDate().isAfter(lastFeeding.date)

    val breast: Int
        get() = lastFeeding.breast

    var isEmpty = false

    init {
        if (feeding == null) {
            lastFeeding = Feeding(
                date = LocalDate.now(),
                timestamp = LocalTime.now()
            )
            isEmpty = true
        } else {
            lastFeeding = feeding
        }

        nextFeedingDate = calculateNextFeeding(nextFeedingHourInterval)
    }

    private fun calculateNextFeeding(nextFeedingHourInterval: Int): LocalDateTime {
        val time = lastFeeding.timestamp
        val date = lastFeeding.date
        val lastFeeding = LocalDateTime.of(date, time)
        return lastFeeding.plusHours(nextFeedingHourInterval.toLong())
    }

    fun hasProbiotics(): Boolean {
        return FlagUtils.HasFlag(lastFeeding.additions, AdditionalConstants.PROBIOTICS)
    }

    fun hasVigantol(): Boolean {
        return FlagUtils.HasFlag(lastFeeding.additions, AdditionalConstants.VIGANTOL)
    }

    fun hasEspumisan(): Boolean {
        return FlagUtils.HasFlag(lastFeeding.additions, AdditionalConstants.ESPUMISAN)
    }
}
