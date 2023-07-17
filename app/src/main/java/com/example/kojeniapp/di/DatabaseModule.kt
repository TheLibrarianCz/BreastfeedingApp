package com.example.kojeniapp.di

import android.content.Context
import androidx.room.Room
import com.example.kojeniapp.backup.FeedingSerializer
import com.example.kojeniapp.backup.Serializer
import com.example.kojeniapp.database.AppDatabase
import com.example.kojeniapp.database.Repository
import com.example.kojeniapp.database.models.Feeding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "breast_feeding_database"

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun provideRepository(
        appDatabase: AppDatabase,
        @ApplicationScope appScope: CoroutineScope
    ): Repository = Repository(appScope, appDatabase)

    @Provides
    @Singleton
    fun provideFeedingSerializer(): Serializer<Feeding> = FeedingSerializer()
}
