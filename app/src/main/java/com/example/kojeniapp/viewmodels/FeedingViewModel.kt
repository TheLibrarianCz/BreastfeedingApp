package com.example.kojeniapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kojeniapp.AppDataStore
import com.example.kojeniapp.database.Repository
import com.example.kojeniapp.database.models.AdditionalConstants
import com.example.kojeniapp.database.models.Feeding
import com.example.kojeniapp.database.models.LastFeedingDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class FeedingViewModel @Inject constructor(
    appDataStore: AppDataStore,
    private val repository: Repository
) : ViewModel() {

    val useDialog: StateFlow<Boolean> = appDataStore.settings.map { it.newFeedingDialog }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppDataStore.Defaults.NEW_FEEDING_DIALOG
    )

    val feedingUiState: StateFlow<FeedingUiState> =
        repository.lastFeeding.combine(appDataStore.settings) { feeding, appSettings ->
            feeding to appSettings.nextFeedingHour
        }.map { (feeding, nextInterval) ->
            LastFeedingDataModel(feeding, nextInterval)
        }.combine(repository.todayFeedings) { lastFeedingModel, feedings ->
            FeedingUiState.Loaded(lastFeedingModel, feedings)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FeedingUiState.Loading
        )

    fun createFeeding(
        left: Boolean,
        right: Boolean,
        probiotics: Boolean,
        vigantol: Boolean,
        espumisan: Boolean
    ) {
        repository.insertNewFeeding(
            Feeding(
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = sumUpBreasts(left, right),
                additions = sumUpAdditions(probiotics, vigantol, espumisan)
            )
        )
    }

    private fun sumUpBreasts(left: Boolean, right: Boolean): Int = listOf(
        if (left) AdditionalConstants.PROBIOTICS else 0,
        if (right) AdditionalConstants.VIGANTOL else 0
    ).sum()

    private fun sumUpAdditions(
        probiotics: Boolean,
        vigantol: Boolean,
        espumisan: Boolean
    ): Int = listOf(
        if (probiotics) AdditionalConstants.PROBIOTICS else 0,
        if (vigantol) AdditionalConstants.VIGANTOL else 0,
        if (espumisan) AdditionalConstants.ESPUMISAN else 0
    ).sum()
}

sealed class FeedingUiState {
    object Loading : FeedingUiState()
    data class Loaded(
        val lastFeedingDataModel: LastFeedingDataModel,
        val feedings: List<Feeding>
    ) : FeedingUiState()
}
