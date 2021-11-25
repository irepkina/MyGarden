package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.PlantPhoto

/**
 * Интерфейс доменного слоя для репозитория фотографий растений.
 *
 * Created by i.repkina on 07.11.2021.
 */
interface PlantPhotoRepository {
    // Получение списка фотографий по идентификатору растения
    fun getAllByPlantId(plantId: Long): List<PlantPhoto>?

    // Добавление фотографии растения
    fun insertPlantPhoto(plantPhoto: PlantPhoto)

    // Удаление фотографии растения
    fun deletePlantPhoto(plantPhotoList: List<PlantPhoto>)

    // Установить фото по умолчанию для растения
    fun updateSelectedPhoto(plantId: Long, plantPhotoId: Long)
}