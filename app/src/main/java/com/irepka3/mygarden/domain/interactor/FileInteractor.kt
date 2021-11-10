package com.irepka3.mygarden.domain.interactor

import android.net.Uri
import java.io.File

/**
 * Интерактор для работы с фотографиями
 *
 * Created by i.repkina on 04.11.2021.
 */
interface FileInteractor {
    /**
     * Копирует внешний файл в локальное хранилище
     * @param directory папка в локальном хранилище, в которой сохранять файл
     * @param externalUri ссылка на внешний файл
      */
    fun copyFileToLocalStorage(directory: File, externalUri: Uri): Uri?

    /**
     * Удаляет файлы из локального хранилища
     * @param localUri ссылка на файл локального хранилища
     */
    fun deleteFile(localUri: Uri)
}