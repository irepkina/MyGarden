package com.irepka3.mygarden.domain.model

/**
 * Модель фото клумбы доменного слоя
 * @property flowerbedPhotoId идентификатор фото клумбы. Может быть null, если фото еще не сохранено в базу
 * @property flowerbedId идентификатор клумбы
 * @property uri ссылка на файл фото клубмы
 *
 * Created by i.repkina on 04.11.2021.
 */
data class FlowerbedPhoto(val flowerbedPhotoId: Long? = null,
                          val flowerbedId: Long = 0,
                          val uri: String = ""
)