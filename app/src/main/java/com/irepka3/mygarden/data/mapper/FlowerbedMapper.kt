package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.FlowerbedEntity
import com.irepka3.mygarden.data.database.FlowerbedWithPhoto
import com.irepka3.mygarden.domain.model.Flowerbed

/**
 * Трансформирует модель доменного слоя [Flowerbed] в запись таблицы [FlowerbedEntity]
 */
fun Flowerbed.toEntity(): FlowerbedEntity {
    val flowerbedEntity = FlowerbedEntity()
    flowerbedEntity.flowerbedId = this.flowerbedId ?: 0
    flowerbedEntity.name = this.name
    flowerbedEntity.description = this.description
    flowerbedEntity.comment = this.comment
    return flowerbedEntity
}

/**
 * Трансформирует запись таблицы [FlowerbedEntity] в модель доменного слоя[Flowerbed]
 */
fun FlowerbedEntity.toDomain(): Flowerbed {
    return Flowerbed(this.flowerbedId, this.name, this.description, this.comment, null)
}

/**
 * Трансформирует запись таблицы [FlowerbedWithPhoto] в модель доменного слоя[Flowerbed]
 */
fun FlowerbedWithPhoto.toDomain(): Flowerbed {
    return Flowerbed(
        flowerbedId = this.flowerbed.flowerbedId,
        name = this.flowerbed.name,
        description = this.flowerbed.description,
        comment = this.flowerbed.comment,
        uri = this.photoUri
    )
}
