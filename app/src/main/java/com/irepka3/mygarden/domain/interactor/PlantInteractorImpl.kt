package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.PlantRepository
import javax.inject.Inject

/**
 * Реализация интерактора для работы с растениями
 *
 * Created by i.repkina on 02.11.2021.
 */
class PlantInteractorImpl @Inject constructor(
    private val plantRepository: PlantRepository,
    private val dirRepository: DirRepository
) : PlantInteractor {

    override fun getPlantsByFlowerbed(flowerbedId: Long): List<Plant> {
        return plantRepository.getPlantsByFlowerbed(flowerbedId).sortedBy { it.plantId }
    }

    override fun getPlant(plantId: Long): Plant {
        return plantRepository.getPlant(plantId)
    }

    override fun insertPlant(plant: Plant): Long {
        return plantRepository.insertPlant(plant)
    }

    override fun updatePlant(plant: Plant) {
        plantRepository.updatePlant(plant)
    }

    override fun deletePlant(plant: Plant) {
        if (plant.plantId == null)
            throw IllegalStateException("Invalid flowerbedId, flowerbedId is null")
        val file = dirRepository.getPlantDir(flowerbedId = plant.flowerbedId, plantId = plant.plantId)
        if (file.exists()) {
            file.deleteRecursively()
        }
        plantRepository.deletePlant(plant)
    }
}