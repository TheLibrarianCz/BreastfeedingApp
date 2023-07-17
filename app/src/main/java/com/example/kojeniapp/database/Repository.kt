package com.example.kojeniapp.database

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.kojeniapp.database.models.Feeding
import com.example.kojeniapp.database.models.StatisticsModel
import com.example.kojeniapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class Repository(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val appDatabase: AppDatabase
) {

    val todayFeedings: Flow<List<Feeding>>
        get() = appDatabase.feedingDao().getByDateFlow(LocalDate.now())

    val lastFeeding: Flow<Feeding>
        get() = appDatabase.feedingDao().getLastFeedingState()

    suspend fun getAllFeedings(): List<Feeding> = appDatabase.feedingDao().getAll()

    suspend fun getStatisticsByQuery(limit: Int): List<StatisticsModel> {
        val args = arrayOf(LocalDate.now().toString(), limit)
        val query = SimpleSQLiteQuery(
            "SELECT date, avg(difference) / 60 AS average, MAX(difference) / 60 AS maximum, COUNT(*) AS total " +
                "FROM (" +
                "   SELECT 	date, (JULIANDAY(timestamp) - JULIANDAY(previous_timestamp)) * 60 * 24 AS difference " +
                "   FROM (" +
                "       SELECT *, LAG(timestamp, 1, 0) OVER (PARTITION BY date ORDER BY timestamp ASC) previous_timestamp" +
                "       FROM feeding " +
                "   )" +
                "   WHERE previous_timestamp > 0 " +
                ") " +
                "WHERE date < ? " +
                "GROUP BY date " +
                "ORDER BY date DESC " +
                "LIMIT ?",
            args
        )
        return appDatabase.feedingDao().getStatisticsByQuery(query)
    }

    fun insertNewFeeding(feeding: Feeding) {
        applicationScope.launch(Dispatchers.IO) {
            appDatabase.feedingDao().insert(feeding)
        }
    }

    fun insertFeedings(feedings: List<Feeding>) {
        applicationScope.launch(Dispatchers.IO) {
            appDatabase.feedingDao().insert(feedings)
        }
    }
}
