package com.irepka3.mygarden.domain.interactor

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.irepka3.mygarden.util.Const.APP_TAG
import java.io.File
import java.util.UUID
import javax.inject.Inject

/**
 * Реализация интерактора для работы с фотографиями
 * @param context контекст
 * Created by i.repkina on 05.11.2021.
 */
class FileInteractotImpl @Inject constructor(
    private val context: Context
) : FileInteractor {

    override fun copyFileToLocalStorage(directory: File, externalUri: Uri): Uri? {
        Log.d(
            TAG,
            "readImage() called with: pathSegments = ${externalUri.pathSegments}, last = ${externalUri.pathSegments.last()}"
        )
        val file = File(directory, UUID.randomUUID().toString())
        val inputStream = context.contentResolver.openInputStream(externalUri)
        return if (inputStream != null) {
            file.createNewFile()
            file.outputStream().use { out -> inputStream.copyTo(out) }
            Log.d(TAG, "readImage() called with: uri = ${file.toUri()}, size = ${file.length()}")
            file.toUri()
        } else {
            null
        }
    }

    override fun deleteFile(localUri: Uri) {
        val path = localUri.path
        if (path != null) {
            File(path).delete()
        }
    }
}

private const val TAG = "${APP_TAG}.PhotoInteractotImpl"