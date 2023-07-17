package com.example.kojeniapp.backup

import com.example.kojeniapp.database.models.Feeding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.concurrent.TimeUnit

interface Serializer<T> {

    fun serialize(items: List<T>): String

    fun deserialize(item: String): List<T>
}

class FeedingSerializer : Serializer<Feeding> {

    override fun serialize(items: List<Feeding>): String {
        val backupFeedings = items.map {
            BackupFeeding(
                id = it.id,
                breast = it.breast,
                additions = it.additions,
                timestamp = dateTimeToTimestamp(it.date, it.timestamp)
            )
        }
        return Gson().toJson(backupFeedings)
    }

    override fun deserialize(item: String): List<Feeding> {
        val sType = object : TypeToken<List<BackupFeeding>>() {}.type
        val backupFeedings = Gson().fromJson<List<BackupFeeding>>(item, sType)
        val feedings = backupFeedings.map { feeding ->
            val (date, time) = timestampToDateTime(feeding.timestamp)
            Feeding(
                id = feeding.id,
                date = date,
                timestamp = time,
                breast = feeding.breast,
                additions = feeding.additions
            )
        }
        return feedings
    }

    private fun dateTimeToTimestamp(date: LocalDate, time: LocalTime): Long {
        val offset = ZoneOffset.ofTotalSeconds(TimeUnit.HOURS.toSeconds(2).toInt())
        return TimeUnit.SECONDS.toMillis(time.toEpochSecond(date, offset))
    }

    private fun timestampToDateTime(timestamp: Long): Pair<LocalDate, LocalTime> {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        with(cal) {
            val time = LocalTime.of(
                get(Calendar.HOUR_OF_DAY),
                get(Calendar.MINUTE),
                get(Calendar.SECOND)
            )
            val date = LocalDate.of(
                get(Calendar.YEAR),
                get(Calendar.MONTH) + 1,
                get(Calendar.DAY_OF_MONTH)
            )
            return date to time
        }
    }

    private data class BackupFeeding(
        val id: Int,
        val breast: Int,
        val additions: Int,
        val timestamp: Long
    )
}
