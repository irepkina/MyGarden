package com.irepka3.mygarden.domain.interactor
import com.irepka3.mygarden.domain.model.FlowerbedPhoto

/**
 * Интерактор для работы со списком фото клумбы
 *
 * Created by i.repkina on 02.11.2021.
 */
interface FlowerbedPhotoInteractor {
    // Получение списка фото клумбы по идентификатору клумбы
    fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto>?

    // Получение фото клумб по идентификатору фото
    fun getFlowerbedPhoto(flowerbedPhotoId: Long): FlowerbedPhoto

    // Добавление фотографии клумбы
    fun insertFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto)

    // Удаление фотографии клумбы
    fun deleteFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto)
}