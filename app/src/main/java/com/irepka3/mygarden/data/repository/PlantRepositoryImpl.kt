package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.PlantEntity
import com.irepka3.mygarden.data.database.PlantWithPhoto
import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.domain.repository.PlantRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Репозиторий для работы с растениями
 * @param database база данных
 *
 * Created by i.repkina on 02.11.2021.
 */
class PlantRepositoryImpl
@Inject constructor(private val database: AppRoomDataBase)
    : PlantRepository {

    init {
        Log.d(TAG, "init called")
    }

    override fun getPlantsByFlowerbed(flowerbedId: Long): List<Plant> {
        Log.d(TAG, "getPlantsByFlowerBed() called with: flowerbed_id = $flowerbedId")
        return database.plantDao.getPlantsByFlowerBedId(flowerbedId)?.map { it.toDomain() } ?: emptyList()
    }

    override fun getPlant(plantId: Long): Plant {
        Log.d(TAG, "getPlant() called with: id = $plantId")
        return database.plantDao.getById(plantId).toDomain()
    }

    override fun insertPlant(plant: Plant): Long {
        Log.d(TAG, "insertPlant() called with: plant = $plant")
        return database.plantDao.insert(plant.toEntity())
    }

    override fun updatePlant(plant: Plant) {
        Log.d(TAG, "updatePlant() called with: plant = $plant")
        return database.plantDao.update(plant.toEntity())
    }

    override fun deletePlant(plant: Plant) {
        Log.d(TAG, "deletePlant() called with: plant = $plant")
        return database.plantDao.delete(plant.toEntity())
    }

    /**
     * Трансформирует модель бизнес-слоя [Plant] в запись таблицы [PlantEntity]
     */
    private fun Plant.toEntity(): PlantEntity {
        val plantEntity = PlantEntity()
        plantEntity.flowerbedId = this.flowerbedId
        plantEntity.plantId = this.plantId ?: 0L
        plantEntity.name = this.name
        plantEntity.description = this.description
        plantEntity.plant_date = this.datePlant
        plantEntity.plantsCount = this.count
        return plantEntity
    }

    /**
     * Трансформирует запись таблицы [PlantEntity] в модель доменного слоя[Plant]
     */
    private fun PlantEntity.toDomain(): Plant {
        Log.d(TAG, "PlantEntity.toDomain() called plantsCount = ${this.plantsCount}")
        return Plant(
            flowerbedId = this.flowerbedId,
            plantId = this.plantId,
            name = this.name,
            description = this.description,
            comment = this.comment,
            count = this.plantsCount,
            datePlant = this.plant_date
        )
    }

    /**
     * Трансформирует запись таблицы [PlantWithPhoto] в модель доменного слоя[Plant]
     */
    private fun PlantWithPhoto.toDomain(): Plant {
        Log.d(TAG, "PlantWithPhoto.toDomain() called plantsCount = ${this.plant.plantsCount}")
        return Plant(
            flowerbedId = this.plant.flowerbedId,
            plantId = this.plant.plantId,
            name = this.plant.name,
            description = this.plant.description,
            comment = this.plant.comment,
            count = this.plant.plantsCount,
            datePlant = this.plant.plant_date,
            uri = this.photoUri
        )
    }
}

private const val  TAG = "${APP_TAG}.PlantRepositoryImpl"