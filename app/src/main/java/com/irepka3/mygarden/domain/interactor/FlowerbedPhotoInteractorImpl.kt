package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository

/**
 * Реализация интеректора для работы с фотографиями клумбы
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoInteractorImpl(private val flowerbedPhotoRepository: FlowerbedPhotoRepository): FlowerbedPhotoInteractor {
    override fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto>? {
        return flowerbedPhotoRepository.getAllByFlowerbedId(flowerbedId)
    }

    override fun getFlowerbedPhoto(flowerbedPhotoId: Long):  FlowerbedPhoto {
        return flowerbedPhotoRepository.getFlowerbedPhoto(flowerbedPhotoId)
    }

    override fun insertFlowerbedPhoto(flowerbedPhoto:  FlowerbedPhoto) {
        flowerbedPhotoRepository.insertFlowerbedPhoto(flowerbedPhoto)
    }
    override fun deleteFlowerbedPhoto(flowerbedPhoto:  FlowerbedPhoto) {
        flowerbedPhotoRepository.deleteFlowerbedPhoto(flowerbedPhoto)
    }
}