package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.PlantPhoto
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.PlantPhotoRepository
import java.io.File
import javax.inject.Inject

/**
 * Реализация интеректора для работы с фотографиями растения
 *
 * Created by i.repkina on 04.11.2021.
 */
class PlantPhotoInteractorImpl @Inject constructor(
    private val dirRepository: DirRepository,
    private val plantPhotoRepository: PlantPhotoRepository
) : PlantPhotoInteractor {

    override fun getAllByPlantId(plantId: Long): List<PlantPhoto>? {
        return plantPhotoRepository.getAllByPlantId(plantId)?.sortedBy { it.order; it.plantPhotoId }
    }

    override fun insertPlantPhoto(plantPhoto: PlantPhoto) {
        plantPhotoRepository.insertPlantPhoto(plantPhoto)
    }

    override fun deletePlantPhoto(plantPhotoList: List<PlantPhoto>) {
        plantPhotoRepository.deletePlantPhoto(plantPhotoList)
    }

    override fun getPlantDir(flowerbedId: Long, plantId: Long): File {
        return dirRepository.getPlantDir(flowerbedId = flowerbedId, plantId = plantId)
    }

    override fun updateSelectedPhoto(plantId: Long, plantPhotoId: Long) {
        plantPhotoRepository.updateSelectedPhoto(plantId = plantId, plantPhotoId = plantPhotoId)
    }
}