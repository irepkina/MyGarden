package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
import com.irepka3.mygarden.domain.model.PlantPhoto
import com.irepka3.mygarden.domain.repository.PlantPhotoRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Репозиторий для работы с фотографиями растения
 * @param database база данных
 *
 * Created by i.repkina on 07.11.2021.
 */
class PlantPhotoRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : PlantPhotoRepository {

    override fun getAllByPlantId(plantId: Long): List<PlantPhoto> {
        Log.d(TAG, "getAllByPlantId() called with: plantId = $plantId")
        return database.plantPhotoDao.getAllByPlantId(plantId)?.map { it.toDomain() } ?: emptyList()
    }

    override fun insertPlantPhoto(plantPhoto: PlantPhoto) {
        Log.d(TAG, "insertPlantPhoto() called with: plantPhoto = $plantPhoto")
        return database.plantPhotoDao.insert(plantPhoto.toEntity())
    }

    override fun deletePlantPhoto(plantPhotoList: List<PlantPhoto>) {
        Log.d(TAG, "deletePlantPhoto() called with: plantPhotoList = $plantPhotoList")
        return database.plantPhotoDao.delete(plantPhotoList.map { it.toEntity() })
    }

    override fun updateSelectedPhoto(plantId: Long, plantPhotoId: Long) {
        Log.d(
            TAG,
            "updateSelectedPhoto() called with: plantId = $plantId, plantPhotoId = $plantPhotoId"
        )
        database.plantPhotoDao.updateSelectedPhoto(plantId = plantId, plantPhotoId = plantPhotoId)
    }
}

private const val TAG = "{$APP_TAG}.PlantPhotoRepositoryImpl"