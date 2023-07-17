package com.example.kojeniapp.backup

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getFileName(fileUri: Uri): String? {
        return if (fileUri.scheme == CONTENT_SCHEME) {
            tryGetFileName(fileUri)
        } else {
            fileUri.pathSegments.last()
        }
    }

    private fun tryGetFileName(fileUri: Uri): String? = try {
        var result: String? = null
        context.contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                result = cursor.getString(
                    cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                )
            }
        }
        result
    } catch (e: IllegalArgumentException) {
        Log.e(TAG, "Error while trying to get files name - ($e).")
        null
    }

    suspend fun readContent(fileUri: Uri): String? = withContext(Dispatchers.IO) {
        Log.d(TAG, "$TAG#readContent().")
        val inputStream = context.contentResolver.openInputStream(fileUri)

        if (inputStream == null) {
            Log.d(TAG, "Failed to open input stream for the given file.")
            null
        } else {
            val content = inputStream.bufferedReader().readText()
            tryClose(inputStream)
            content
        }
    }

    private fun tryClose(inputStream: InputStream) {
        try {
            inputStream.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error while trying to close the file - ($e).")
        }
    }

    suspend fun writeContent(fileName: String, content: String) = withContext(Dispatchers.IO) {
        Log.d(TAG, "$TAG#writeContent($fileName)")
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        try {
            val file = File(path, "$fileName.txt")

            if (file.createNewFile()) {
                FileOutputStream(file).use { it.write(content.toByteArray()) }
                Log.i(TAG, "$TAG: Wrote to file: $fileName")
            } else {
                Log.w(TAG, "$TAG: File already exists. Ignored.")
            }
        } catch (e: IOException) {
            Log.e(TAG, "$TAG Error: $e")
        }
    }

    companion object {
        private const val TAG = "AndroidFileManager"

        private const val CONTENT_SCHEME = "content"
    }
}
