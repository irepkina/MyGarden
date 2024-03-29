package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Репозиторий для работы с фотографиями клумбы
 * @param database база данных
 *
 * Created by i.repkina on 04.11.2021.
 */

class FlowerbedPhotoRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : FlowerbedPhotoRepository {

    override fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhoto> {
        Log.d(TAG, "getAllByFlowerbedId() called with: flowerbedId = $flowerbedId")
        return database.flowerbedPhotoDao.getAllByFlowerbedId(flowerbedId)?.map { it.toDomain() }
            ?: emptyList()
    }

    override fun insertFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto) {
        Log.d(TAG, "insertFlowerbedPhoto() called with: flowerbedPhoto = $flowerbedPhoto")
        return database.flowerbedPhotoDao.insert(flowerbedPhoto.toEntity())
    }

    override fun deleteFlowerbedPhoto(flowerbedPhotoList: List<FlowerbedPhoto>) {
        Log.d(TAG, "deleteFlowerbedPhoto() called with: flowerbedPhotoList = $flowerbedPhotoList")
        database.flowerbedPhotoDao.delete(flowerbedPhotoList.map { it.toEntity() })
    }

    override fun updateSelectedPhoto(flowerbedId: Long, flowerbedPhotoId: Long) {
        Log.d(
            TAG,
            "updateSelectedPhoto() called with: flowerbedId = $flowerbedId, flowerbedPhotoId = $flowerbedPhotoId"
        )
        database.flowerbedPhotoDao.updateSelectedPhoto(
            flowerbedId = flowerbedId,
            flowerbedPhotoId = flowerbedPhotoId
        )
    }
}

private const val TAG = "{$APP_TAG}.FlowerbedPhotoRepositoryImpl"