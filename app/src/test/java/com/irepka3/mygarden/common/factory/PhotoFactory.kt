package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.ui.photo.model.Photo

/**
 * Фабрика для создания экземпляров класса [Photo]
 *
 * Created by i.repkina on 23.11.2021.
 */
class PhotoFactory {
    companion object {
        /**
         * Создает экземпляр [Photo]
         * @param photoId идентификатор фото
         * @param uri URI файла фото
         * @param selected фото по умолчанию
         */
        fun createPhoto(photoId: Long?, uri: String?, selected: Boolean?): Photo {
            return Photo(photoId, uri ?: "", selected ?: false)
        }
    }
}