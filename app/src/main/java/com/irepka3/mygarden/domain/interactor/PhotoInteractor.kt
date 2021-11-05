package com.irepka3.mygarden.domain.interactor

import android.net.Uri

/**
 * Интерактор для работы с фотографиями
 *
 * Created by i.repkina on 04.11.2021.
 */
interface PhotoInteractor {
    /**
     * Копирует внешний файл в локальное хранилище
     * @param externalUri ссфлка на внешний файл
      */
    fun copyFileToLocalStorage(externalUri: Uri): Uri?
}