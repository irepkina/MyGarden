package com.irepka3.mygarden.domain.interactor
import com.irepka3.mygarden.domain.model.Plant

/**
 * Интерактор для работы со списком растений
 *
 * Created by i.repkina on 02.11.2021.
 */
interface PlantInteractor {
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