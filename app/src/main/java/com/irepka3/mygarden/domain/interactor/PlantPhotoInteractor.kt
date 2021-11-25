package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.PlantPhoto
import java.io.File

/**
 * Интерактор для работы со списком фото растений
 *
 * Created by i.repkina on 07.11.2021.
 */
interface PlantPhotoInteractor {
    // Получение списка фото растений по идентификатору растения
    fun getAllByPlantId(plantId: Long): List<PlantPhoto>?

    // Добавление фотографии клумбы
    fun insertPlantPhoto(plantPhoto: PlantPhoto)

    // Удаление фотографий клумбы
    fun deletePlantPhoto(plantPhotoList: List<PlantPhoto>)

    // Возвращает папку для фотографий растения
    fun getPlantDir(flowerbedId: Long, plantId: Long): File

    // Установить фото по умолчанию для растения
    fun updateSelectedPhoto(plantId: Long, plantPhotoId: Long)

}