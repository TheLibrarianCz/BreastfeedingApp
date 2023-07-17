package com.example.kojeniapp.backup

import android.util.Log
import java.util.Calendar

object FileNameGenerator {

    private const val FILE_FORMAT = "kApp_%s_%s_%s_%s"
    private const val TAG = "BackupManager"

    fun generateFileName(): String {
        Log.d(TAG, "$TAG#generateFilename()")
        val cal = Calendar.getInstance()

        return FILE_FORMAT.format(
            cal.get(Calendar.HOUR),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH)
        )
    }
}
