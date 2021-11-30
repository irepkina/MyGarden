package com.irepka3.mygarden.ui.photo.model

/**
 * Фотография
 * @property photoId идентифкатор фотографии
 * @property uri uri фото
 * @property selected выбранное фото по умолчанию
 * @property deleted выбранное фото для удаления
 *
 * Created by i.repkina on 07.11.2021.
 */
data class Photo(
    val photoId: Long? = null,
    val uri: String = "",
    var selected: Boolean = false,
    var deleted: Boolean = false
)
