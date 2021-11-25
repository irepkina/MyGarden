package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import java.io.File

/**
 * Интерактор для работы со списком фото клумбы
 *
 * Created by i.repkina on 02.11.2021.
 */
interface FlowerbedPhotoInteractor {
    // Получение списка фото клумбы по идентификатору клумбы
    fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto>?

    // Добавление фотографии клумбы
    fun insertFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto)

    // Удаление фотографий клумбы
    fun deleteFlowerbedPhoto(flowerbedPhotoList: List<FlowerbedPhoto>)

    // Установить фото по умолчанию для клумбы
    fun updateSelectedPhoto(flowerbedId: Long, flowerbedPhotoId: Long)

    // Возвращает папку для фотографий клумбы
    fun getFlowerbedDir(flowerbedId: Long): File
}