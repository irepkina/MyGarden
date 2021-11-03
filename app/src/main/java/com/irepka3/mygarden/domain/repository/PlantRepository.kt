package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.Plant

/**
 * Интерфейс доменного слоя для репозитория растений.
 *
 * Created by i.repkina on 02.11.2021.
 */
interface PlantRepository {
    // Получение списка растений
    fun getPlantsAll(): List<Plant>

    // Получение списка растений по идентификатору клумбы
    fun getPlantsByFlowerbed(flowerbedId: Long): List<Plant>

    // Получение растения по идентификатору
    fun getPlant(plantId: Long): Plant

    // Добавление растения
    fun insertPlant(plant: Plant)

    // Обновление данных растения
    fun updatePlant(plant: Plant)

    // Удаление растения
    fun deletePlant(plant: Plant)
}