package com.irepka3.mygarden.domain.model


/**
 * Модель фото растения доменного слоя
 * @property plantId идентификатор растения
 * @property plantPhotoId идентификатор фото растения. Может быть null, если фото еще не сохранено в базу
 * @property uri ссылка на файл фото клубмы
 *@property selected выбрано для удаления
 *
 * Created by i.repkina on 07.11.2021.
 */
data class PlantPhoto(val plantId: Long = 0,
                      val plantPhotoId: Long? = null,
                      val uri: String = "",
                      var selected: Boolean = false
)
