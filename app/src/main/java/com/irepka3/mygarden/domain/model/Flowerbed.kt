package com.irepka3.mygarden.domain.model

/**
 * Модель клумбы доменного слоя
 * @property flowerbedId идентификатор клумбы. Может быть null, если клумба еще не сохранена в базу
 * @property name название клумбы
 * @property description описание клумбы
 * @property comment комментарий к клумбе
 *
 * Created by i.repkina on 31.10.2021.
 */
data class Flowerbed(val flowerbedId: Long? = null,
                     val name:String = "",
                     val description:String = "",
                     val comment:String? = null
)
