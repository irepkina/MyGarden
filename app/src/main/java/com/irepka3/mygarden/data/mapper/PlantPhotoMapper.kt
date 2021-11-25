package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.PlantPhotoEntity
import com.irepka3.mygarden.domain.model.PlantPhoto

/**
 * Трансформирует запись таблицы [PlantPhotoEntity] в модель доменного слоя[PlantPhoto]
 */
fun PlantPhotoEntity.toDomain(): PlantPhoto {
    return PlantPhoto(
        plantId = this.plantId,
        plantPhotoId = this.plantPhotoId,
        uri = this.uri,
        selected = this.selected,
        order = this.order
    )
}

/**
 * Трансформирует модель доменного слоя [PlantPhoto] в запись таблицы [PlantPhotoEntity]
 */
fun PlantPhoto.toEntity(): PlantPhotoEntity {
    val PlantPhotoEntity = PlantPhotoEntity()
    PlantPhotoEntity.plantPhotoId = this.plantPhotoId ?: 0L
    PlantPhotoEntity.plantId = this.plantId
    PlantPhotoEntity.uri = this.uri
    PlantPhotoEntity.selected = this.selected
    PlantPhotoEntity.order = this.order
    return PlantPhotoEntity
}