package com.example.kojeniapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.kojeniapp.database.models.Feeding
import com.example.kojeniapp.database.models.StatisticsModel
import com.example.kojeniapp.database.models.StatisticsSubmodel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface FeedingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg feedings: Feeding)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(feedings: List<Feeding>)

    @Delete
    fun delete(feeding: Feeding)

    @Query("SELECT * FROM feeding WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: LocalDate?): LiveData<List<Feeding>>

    @Query("SELECT * FROM feeding WHERE date = :date ORDER BY timestamp DESC")
    fun getByDateFlow(date: LocalDate?): Flow<List<Feeding>>

    @Query("SELECT * FROM feeding WHERE date = :date ORDER BY timestamp DESC")
    fun getByDateState(date: LocalDate?): Flow<List<Feeding>>

    @Query("SELECT * FROM feeding ORDER BY date DESC, timestamp DESC LIMIT 1")
    fun getLastFeeding(): LiveData<Feeding>

    @Query("SELECT * FROM feeding ORDER BY date DESC, timestamp DESC LIMIT 1")
    fun getLastFeedingState(): Flow<Feeding>

    @Query("SELECT * FROM feeding")
    suspend fun getAll(): List<Feeding>

    @Query("SELECT * FROM feeding")
    fun getAllFlow(): Flow<List<Feeding>>

    @RawQuery(observedEntities = [Feeding::class])
    suspend fun getStatisticsByQuery(query: SupportSQLiteQuery): List<StatisticsModel>

    @RawQuery(observedEntities = [Feeding::class])
    fun getStatisticsByQueryFlow(query: SupportSQLiteQuery): Flow<List<StatisticsModel>>

    @RawQuery
    fun getStatisticsSubmodelByQuery(query: SupportSQLiteQuery): List<StatisticsSubmodel>
}
