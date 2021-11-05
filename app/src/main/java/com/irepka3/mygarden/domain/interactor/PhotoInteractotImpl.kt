package com.irepka3.mygarden.domain.interactor

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.irepka3.mygarden.util.Const.APP_TAG
import java.io.File

/**
 * Реализация интерактора для работы с фотографиями
 * @param context контекст
 * Created by i.repkina on 05.11.2021.
 */
class PhotoInteractotImpl(private val context: Context): PhotoInteractor {
    private val imagesDir = File(context.filesDir, IMAGE_FOLDER)

    override fun copyFileToLocalStorage(externalUri: Uri): Uri? {

        imagesDir.mkdirs()
        Log.d(TAG, "readImage() called with: pathSegments = ${externalUri.pathSegments}, last = ${externalUri.pathSegments.last()}")
        val file = File(imagesDir, externalUri.pathSegments.last())
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
}

private const val TAG = "${APP_TAG}.PhotoInteractotImpl"
private const val IMAGE_FOLDER = "mygarden.images"