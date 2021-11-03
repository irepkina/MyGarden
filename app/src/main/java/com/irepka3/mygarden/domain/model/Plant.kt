package com.irepka3.mygarden.domain.model

/**
 * Модель растения доменного слоя
 * @property plantId идентификатор растения. Может быть null, если растение еще не сохранено в базу
 * @property flowerbedId идентификатор клумбы
 * @property name название растения
 * @property description описание растения
 *
 * Created by i.repkina on 02.11.2021.
 */
data class Plant(val plantId: Long? = null,
                 val flowerbedId: Long = 0,
                 val name: String = "",
                 val description:String = "",
                 val count: Int = 1,
                 val datePlant: Long=0
)
