package com.example.kojeniapp.backup

import android.net.Uri
import android.util.Log
import com.example.kojeniapp.database.Repository
import com.example.kojeniapp.database.models.Feeding
import com.example.kojeniapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupManager @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val androidFileReader: AndroidFileManager,
    private val feedingSerializer: Serializer<Feeding>,
    private val repository: Repository
) {

    private val _state: MutableStateFlow<BackupManagerState> =
        MutableStateFlow(BackupManagerState.Idle)
    val state: StateFlow<BackupManagerState> = _state.asStateFlow()

    private var exportingJob: Job? = null

    private var importJob: Job? = null

    fun import(fileUri: Uri) {
        Log.d(TAG, "$TAG#export()")
        val currentExportJobRunning = exportingJob?.isActive ?: false
        val currentImportJobRunning = importJob?.isActive ?: false
        if (!currentExportJobRunning && !currentImportJobRunning) {
            importJob = createImportJob(fileUri)
        } else {
            Log.w(TAG, "$TAG: Trying to start another import job.")
        }
    }

    private fun createImportJob(fileUri: Uri): Job = applicationScope.launch(Dispatchers.IO) {
        Log.d(TAG, "$TAG#createImportJob()")
        val fileName = androidFileReader.getFileName(fileUri)
        _state.value = BackupManagerState.Importing(importFileName = fileName ?: "")
        val content = androidFileReader.readContent(fileUri)

        if (content != null) {
            import(feedings = feedingSerializer.deserialize(content))
        }

        delay(1000)
    }.apply {
        invokeOnCompletion {
            Log.d(TAG, "$TAG: Importing finished.")
            _state.value = BackupManagerState.Idle
        }
    }

    private suspend fun import(feedings: List<Feeding>): Unit = withContext(Dispatchers.IO) {
        if (feedings.isNotEmpty()) {
            repository.insertFeedings(feedings)
            Log.i(TAG, "$TAG: Import finished.")
        } else {
            Log.i(TAG, "$TAG: Import empty.")
        }
    }

    fun export() {
        Log.d(TAG, "$TAG#export()")
        val currentExportJobRunning = exportingJob?.isActive ?: false
        val currentImportJobRunning = importJob?.isActive ?: false
        if (!currentExportJobRunning && !currentImportJobRunning) {
            exportingJob = createExportingJob()
        } else {
            Log.w(TAG, "$TAG: Trying to start another export job.")
        }
    }

    private fun createExportingJob() = applicationScope.launch(Dispatchers.IO) {
        Log.d(TAG, "$TAG#createExportingJob()")
        val fileName = FileNameGenerator.generateFileName()
        _state.value = BackupManagerState.Exporting(fileName)

        androidFileReader.writeContent(
            fileName = fileName,
            content = feedingSerializer.serialize(repository.getAllFeedings())
        )

        delay(1000)
    }.apply {
        invokeOnCompletion {
            Log.d(TAG, "$TAG: Exporting finished.")
            _state.value = BackupManagerState.Idle
        }
    }

    companion object {
        const val TAG = "BackupManager"
    }
}

sealed class BackupManagerState {
    object Idle : BackupManagerState()
    data class Importing(val importFileName: String) : BackupManagerState()
    data class Exporting(val exportFileName: String) : BackupManagerState()
}
