package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.PlantEntity
import com.irepka3.mygarden.data.database.PlantWithPhoto
import com.irepka3.mygarden.domain.model.Plant

/**
 * Created by i.repkina on 22.11.2021.
 */
/**
 * Трансформирует модель бизнес-слоя [Plant] в запись таблицы [PlantEntity]
 */
fun Plant.toEntity(): PlantEntity {
    val plantEntity = PlantEntity()
    plantEntity.flowerbedId = this.flowerbedId
    plantEntity.plantId = this.plantId ?: 0L
    plantEntity.name = this.name
    plantEntity.description = this.description
    plantEntity.comment = this.comment
    plantEntity.datePlant = this.datePlant
    plantEntity.plantsCount = this.count
    return plantEntity
}

/**
 * Трансформирует запись таблицы [PlantEntity] в модель доменного слоя[Plant]
 */
fun PlantEntity.toDomain(): Plant {
    return Plant(
        flowerbedId = this.flowerbedId,
        plantId = this.plantId,
        name = this.name,
        description = this.description,
        comment = this.comment,
        count = this.plantsCount,
        datePlant = this.datePlant
    )
}

/**
 * Трансформирует запись таблицы [PlantWithPhoto] в модель доменного слоя[Plant]
 */
fun PlantWithPhoto.toDomain(): Plant {
    return Plant(
        flowerbedId = this.plant.flowerbedId,
        plantId = this.plant.plantId,
        name = this.plant.name,
        description = this.plant.description,
        comment = this.plant.comment,
        count = this.plant.plantsCount,
        datePlant = this.plant.datePlant,
        uri = this.photoUri
    )
}