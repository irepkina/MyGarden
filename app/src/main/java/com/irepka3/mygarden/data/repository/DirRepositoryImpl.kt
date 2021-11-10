package com.irepka3.mygarden.data.repository

import android.content.Context
import com.irepka3.mygarden.domain.repository.DirRepository
import java.io.File
import javax.inject.Inject

/**
 * Реализация репозитория возвращающего папки локального хранилища
 *
 * Created by i.repkina on 09.11.2021.
 */
class DirRepositoryImpl @Inject constructor(context: Context): DirRepository {
    private val imagesDir = File(context.filesDir, IMAGE_FOLDER)

    override fun getCashDir(): File = imagesDir.also { it.mkdirs() }

    override fun getFlowerbedDir(flowerbedId: Long): File {
        return File(getCashDir(), flowerbedId.toString()).also { it.mkdirs() }
    }

    override fun getPlantDir(flowerbedId: Long, plantId: Long): File {
        return File(getFlowerbedDir(flowerbedId), plantId.toString()).also { it.mkdirs() }
    }
}

private const val IMAGE_FOLDER = "mygarden.images"