package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.FlowerbedPhotoEntity
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository

/**
 * Репозиторий для работы с фотографиями клумбы
 * @param database база данных
 *
 * Created by i.repkina on 04.11.2021.
 */

class FlowerbedPhotoRepositoryImpl(private val database: AppRoomDataBase): FlowerbedPhotoRepository {
    override fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto> {
        return database.flowerbedPhotoDao.getAllByFlowerbedId(flowerbedId)?.map { it.toDomain() } ?: emptyList()
    }

    override fun getFlowerbedPhoto(flowerbedPhotoId: Long): FlowerbedPhoto {
        return database.flowerbedPhotoDao.getById(flowerbedPhotoId).toDomain()
    }

    override fun insertFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto) {
        return database.flowerbedPhotoDao.insert(flowerbedPhoto.toEntity())
    }

    override fun deleteFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto) {
        return database.flowerbedPhotoDao.delete(flowerbedPhoto.toEntity())
    }

    private fun FlowerbedPhotoEntity.toDomain(): FlowerbedPhoto{
        return FlowerbedPhoto(this.flowerbedPhotoId, this.flowerbedId, this.uri)
    }

    private fun FlowerbedPhoto.toEntity(): FlowerbedPhotoEntity{
        val flowerbedPhotoEntity = FlowerbedPhotoEntity()
        flowerbedPhotoEntity.flowerbedPhotoId = this.flowerbedPhotoId ?: 0L
        flowerbedPhotoEntity.flowerbedId = this.flowerbedId
        flowerbedPhotoEntity.uri = this.uri
        return  flowerbedPhotoEntity
    }
}