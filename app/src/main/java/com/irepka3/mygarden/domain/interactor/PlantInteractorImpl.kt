package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.domain.repository.PlantRepository

/**
 * Реализация интеркатора для работы с растениями
 *
 * Created by i.repkina on 02.11.2021.
 */
class PlantInteractorImpl(private val plantRepository: PlantRepository): PlantInteractor {
    override fun getPlantsAll(): List<Plant> {
        return plantRepository.getPlantsAll()
    }

    override fun getPlantsByFlowerbed(flowerbedId: Long): List<Plant> {
        return plantRepository.getPlantsByFlowerbed(flowerbedId)
    }

    override fun getPlant(plantId: Long): Plant {
        return plantRepository.getPlant(plantId)
    }

    override fun insertPlant(plant: Plant) {
        plantRepository.insertPlant(plant)
    }

    override fun updatePlant(plant: Plant) {
        plantRepository.updatePlant(plant)
    }

    override fun deletePlant(plant: Plant) {
        plantRepository.deletePlant(plant)
    }
}