package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
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
class PlantRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : PlantRepository {

    override fun getPlantsByFlowerbed(flowerbedId: Long): List<Plant> {
        Log.d(TAG, "getPlantsByFlowerBed() called with: flowerbed_id = $flowerbedId")
        return database.plantDao.getPlantsByFlowerBedId(flowerbedId)?.map { it.toDomain() }
            ?: emptyList()
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
}

private const val TAG = "${APP_TAG}.PlantRepositoryImpl"