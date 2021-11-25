package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import java.io.File
import javax.inject.Inject

/**
 * Реализация интеректора для работы с фотографиями клумбы
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoInteractorImpl @Inject constructor(
    private val dirRepository: DirRepository,
    private val flowerbedPhotoRepository: FlowerbedPhotoRepository
) : FlowerbedPhotoInteractor {

    override fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto>? {
        return flowerbedPhotoRepository.getAllByFlowerbedId(flowerbedId)
            ?.sortedBy { it.flowerbedPhotoId }
    }

    override fun insertFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto) {
        flowerbedPhotoRepository.insertFlowerbedPhoto(flowerbedPhoto)
    }

    override fun deleteFlowerbedPhoto(flowerbedPhotoList: List<FlowerbedPhoto>) {
        flowerbedPhotoRepository.deleteFlowerbedPhoto(flowerbedPhotoList)
    }

    override fun updateSelectedPhoto(flowerbedId: Long, flowerbedPhotoId: Long) {
        flowerbedPhotoRepository.updateSelectedPhoto(
            flowerbedId = flowerbedId,
            flowerbedPhotoId = flowerbedPhotoId
        )
    }

    override fun getFlowerbedDir(flowerbedId: Long): File {
        return dirRepository.getFlowerbedDir(flowerbedId)
    }
}