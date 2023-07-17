package com.example.kojeniapp.viewmodels

import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kojeniapp.AppDataStore
import com.example.kojeniapp.R
import com.example.kojeniapp.backup.BackupManager
import com.example.kojeniapp.backup.BackupManagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appDataStore: AppDataStore,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(listOf())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    val isImporting: StateFlow<Boolean> = backupManager.state
        .map { it is BackupManagerState.Importing }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    val isExporting: StateFlow<Boolean> = backupManager.state
        .map { it is BackupManagerState.Exporting }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    val nextFeedingHour: StateFlow<Int> = appDataStore.settings
        .map { settings -> settings.nextFeedingHour }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            AppDataStore.Defaults.NEXT_FEEDING_HOUR
        )

    val statisticsHistory: StateFlow<Int> = appDataStore.settings
        .map { settings -> settings.historyLength }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            AppDataStore.Defaults.HISTORY_LENGTH
        )

    val newFeedingDialog: StateFlow<Boolean> = appDataStore.settings
        .map { settings -> settings.newFeedingDialog }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            AppDataStore.Defaults.NEW_FEEDING_DIALOG
        )

    init {
        viewModelScope.launch {
            var previousState: BackupManagerState = BackupManagerState.Idle

            backupManager.state.collect { newState ->
                if (previousState != newState) {
                    Log.d(TAG, "$TAG: previous state: $previousState - new state: $newState")
                    handleNewState(previousState, newState)
                    previousState = newState
                }
            }
        }
    }

    private fun handleNewState(previousState: BackupManagerState, newState: BackupManagerState) {
        when (newState) {
            is BackupManagerState.Exporting -> {
                Log.d(TAG, "$TAG: State - Exporting.")
            }

            BackupManagerState.Idle -> {
                Log.d(TAG, "$TAG: State - Idle.")
                handleActiveToInactiveState(previousState)
            }

            is BackupManagerState.Importing -> {
                Log.d(TAG, "$TAG: State - Importing.")
            }
        }
    }

    private fun handleActiveToInactiveState(previousState: BackupManagerState) {
        Log.d(TAG, "$TAG#handleActiveToInactiveState().")
        when (previousState) {
            is BackupManagerState.Exporting -> {
                _messages.value += Message(
                    messageId = R.string.settings_message_export,
                    arg = previousState.exportFileName
                )
            }

            BackupManagerState.Idle -> {
                Log.e(TAG, "$TAG: Duplicate Idle state occurred.")
            }

            is BackupManagerState.Importing -> {
                _messages.value += Message(
                    messageId = R.string.settings_message_import,
                    arg = previousState.importFileName
                )
            }
        }
    }

    fun setNextFeedingHour(newValue: Int) {
        Log.d(TAG, "$TAG#setNextFeedingHour($newValue)")
        viewModelScope.launch {
            appDataStore.setNextFeedingHour(newValue)
        }
    }

    fun setStatisticsHistory(newValue: Int) {
        Log.d(TAG, "$TAG#setStatisticsHistory($newValue)")
        viewModelScope.launch {
            appDataStore.setHistoryLength(newValue)
        }
    }

    fun setNewFeedingDialog(newValue: Boolean) {
        Log.d(TAG, "$TAG#setNewFeedingDialog($newValue)")
        viewModelScope.launch {
            appDataStore.setNewFeedingDialog(newValue)
        }
    }

    fun import(uri: Uri) {
        Log.d(TAG, "$TAG#import()")
        backupManager.import(uri)
    }

    fun export() {
        Log.d(TAG, "$TAG#export()")
        backupManager.export()
    }

    fun onFirstMessageShown() {
        Log.d(TAG, "$TAG#onFirstMessageShown()")
        _messages.value = _messages.value.subList(1, _messages.value.size)
    }

    fun onPermissionError() {
        Log.d(TAG, "$TAG#onPermissionError()")
        _messages.value += Message(messageId = R.string.settings_message_permission)
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}

data class Message(@StringRes val messageId: Int, val arg: String? = null)
