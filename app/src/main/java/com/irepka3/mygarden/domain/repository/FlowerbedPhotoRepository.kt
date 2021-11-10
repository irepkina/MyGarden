package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.FlowerbedPhoto

/**
 * Интерфейс доменного слоя для репозитория фотографий клумбы.
 *
 * Created by i.repkina on 04.11.2021.
 */
interface FlowerbedPhotoRepository {
    // Получение списка фотографий по идентификатору клумбы
    fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto>?

    // Добавление фотографии клумбы
    fun insertFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto)

    // Удаление фотографии клумбы
    fun deleteFlowerbedPhoto(flowerbedPhotoList: List<FlowerbedPhoto>)

    // Установить фото по умолчанию для клумбы
    fun updateSelectedPhoto(flowerbedId: Long, flowerbedPhotoId: Long)
}