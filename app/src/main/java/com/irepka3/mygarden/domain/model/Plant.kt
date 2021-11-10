package com.irepka3.mygarden.domain.model

/**
 * Модель растения доменного слоя
 * @property flowerbedId идентификатор клумбы
 * @property plantId идентификатор растения. Может быть null, если растение еще не сохранено в базу
 * @property name название растения
 * @property description описание растения
 *
 * Created by i.repkina on 02.11.2021.
 */
data class Plant(
                 val flowerbedId: Long = 0,
                 val plantId: Long? = null,
                 val name: String = "",
                 val description:String = "",
                 val comment:String? = null,
                 val count: Int = 1,
                 val datePlant: Long?=null,
                 val uri: String? = null
)
