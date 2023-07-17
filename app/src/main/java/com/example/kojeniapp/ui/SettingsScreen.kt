package com.example.kojeniapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kojeniapp.R
import com.example.kojeniapp.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val context = LocalContext.current

    val requestPermissionLauncher: ActivityResultLauncher<String> =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                settingsViewModel.export()
            } else {
                settingsViewModel.onPermissionError()
            }
        }
    val onExportClick: () -> Unit = {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ==
            PackageManager.PERMISSION_GRANTED
        ) {
            settingsViewModel.export()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            settingsViewModel.import(it)
        }
    }
    val onImportClick: () -> Unit = {
        importLauncher.launch("*/*")
    }

    val statistics by settingsViewModel.statisticsHistory.collectAsState()
    val useDialog by settingsViewModel.newFeedingDialog.collectAsState()
    val nextFeedingHour by settingsViewModel.nextFeedingHour.collectAsState()
    val isExporting by settingsViewModel.isExporting.collectAsState()
    val isImporting by settingsViewModel.isImporting.collectAsState()
    val messages by settingsViewModel.messages.collectAsState()

    SettingsScreen(
        nextFeedingHour = nextFeedingHour,
        setNextFeedingHour = settingsViewModel::setNextFeedingHour,
        statistics = statistics,
        onStatisticsChange = settingsViewModel::setStatisticsHistory,
        useDialog = useDialog,
        onUseDialogChange = settingsViewModel::setNewFeedingDialog,
        isExporting = isExporting,
        onExportClick = onExportClick,
        isImporting = isImporting,
        onImportClick = onImportClick
    )

    if (messages.isNotEmpty()) {
        val message = remember(messages) { messages.first() }
        val messageText = if (message.arg == null) {
            stringResource(id = message.messageId)
        } else {
            stringResource(id = message.messageId, message.arg)
        }
        LaunchedEffect(message, snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = messageText
            )
            settingsViewModel.onFirstMessageShown()
        }
    }
}

@Composable
fun SettingsScreen(
    nextFeedingHour: Int,
    setNextFeedingHour: (Int) -> Unit,
    statistics: Int,
    onStatisticsChange: (Int) -> Unit,
    useDialog: Boolean,
    onUseDialogChange: (Boolean) -> Unit,
    isExporting: Boolean,
    onExportClick: () -> Unit,
    isImporting: Boolean,
    onImportClick: () -> Unit
) {
    Column {
        SliderAppRow(
            title = stringResource(id = R.string.settings_feeding_hour_title),
            description = stringResource(
                id = R.string.settings_statistics_history_description,
                nextFeedingHour
            ),
            painter = painterResource(id = R.drawable.ic_time),
            value = nextFeedingHour,
            onValueChange = setNextFeedingHour,
            valueRange = 2f..6f,
            steps = 5
        )
        SliderAppRow(
            title = stringResource(id = R.string.settings_statistics_history_title),
            description = stringResource(
                id = R.string.settings_use_dialog_description,
                statistics
            ),
            painter = painterResource(id = R.drawable.ic_chart),
            value = statistics,
            onValueChange = onStatisticsChange,
            valueRange = 0f..10f,
            steps = 11
        )
        SwitchAppRow(
            title = stringResource(id = R.string.settings_feeding_hour_description),
            description = stringResource(id = R.string.settings_use_dialog_title),
            painter = painterResource(id = R.drawable.ic_chart),
            checked = useDialog,
            onCheckedChange = onUseDialogChange
        )
        ProgressAppRow(
            title = stringResource(id = R.string.settings_export_title),
            description = stringResource(id = R.string.settings_export_description),
            painter = painterResource(id = R.drawable.ic_export),
            showProgress = isExporting,
            onRowClick = onExportClick,
            rowClickable = !isImporting && !isExporting
        )
        ProgressAppRow(
            title = stringResource(id = R.string.settings_import_title),
            description = stringResource(id = R.string.settings_import_description),
            painter = painterResource(id = R.drawable.ic_import),
            showProgress = isImporting,
            onRowClick = onImportClick,
            rowClickable = !isImporting && !isExporting
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(
        nextFeedingHour = 4,
        setNextFeedingHour = { },
        statistics = 4,
        onStatisticsChange = { },
        useDialog = true,
        onUseDialogChange = { },
        isExporting = false,
        onExportClick = { },
        isImporting = false,
        onImportClick = { }
    )
}
