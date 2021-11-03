package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.PlantEntity
import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.domain.repository.PlantRepository
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Репозиторий для работы с растениями
 * @param database база данных
 *
 * Created by i.repkina on 02.11.2021.
 */
class PlantRepositoryImpl(private val database: AppRoomDataBase): PlantRepository {
    override fun getPlantsAll(): List<Plant> {
        Log.d(TAG, "getPlantsAll() called")
        return database.plantDao.getAll()?.map { it.toDomain() } ?: emptyList()
    }

    override fun getPlantsByFlowerbed(flowerbedId: Long): List<Plant> {
        Log.d(TAG, "getPlantsByFlowerBed() called with: flowerbed_id = $flowerbedId")
        return database.plantDao.getPlantsByFlowerBedId(flowerbedId)?.map { it.toDomain() } ?: emptyList()
    }

    override fun getPlant(plantId: Long): Plant {
        Log.d(TAG, "getPlant() called with: id = $plantId")
        return database.plantDao.getById(plantId).toDomain()
    }

    override fun insertPlant(plant: Plant) {
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
        plantEntity.plantId = this.plantId ?: 0L
        plantEntity.flowerbedId = this.flowerbedId
        plantEntity.name = this.name
        plantEntity.description = this.description
        plantEntity.plant_date = this.datePlant
        plantEntity.count = this.count
        return plantEntity
    }

    /**
     * Трансформирует запись таблицы [PlantEntity] в модель доменного слоя[Plant]
     */
    private fun PlantEntity.toDomain(): Plant {
        return Plant(this.plantId, this.flowerbedId, this.name, this.description, this.count, this.plant_date)
    }
}

private const val  TAG = "${APP_TAG}.PlantRepositoryImpl"