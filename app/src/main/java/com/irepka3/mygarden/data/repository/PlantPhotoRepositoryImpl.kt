package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.PlantPhotoEntity
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
class PlantPhotoRepositoryImpl
@Inject constructor(private val database: AppRoomDataBase)
    : PlantPhotoRepository {

    init {
        Log.d(TAG, "init called")
    }

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
        return database.plantPhotoDao.delete(plantPhotoList.map { it.toEntity()})
    }

    override fun updateSelectedPhoto(plantId: Long, plantPhotoId: Long) {
        Log.d(TAG,"updateSelectedPhoto() called with: plantId = $plantId, plantPhotoId = $plantPhotoId")
        database.plantPhotoDao.updateSelectedPhoto(plantId = plantId, plantPhotoId = plantPhotoId)
    }

    private fun PlantPhotoEntity.toDomain(): PlantPhoto{
        return PlantPhoto(plantId = this.plantId, plantPhotoId = this.plantPhotoId, this.uri, this.selected)
    }

    private fun PlantPhoto.toEntity(): PlantPhotoEntity{
        val PlantPhotoEntity = PlantPhotoEntity()
        PlantPhotoEntity.plantPhotoId = this.plantPhotoId ?: 0L
        PlantPhotoEntity.plantId = this.plantId
        PlantPhotoEntity.uri = this.uri
        PlantPhotoEntity.selected = this.selected
        return  PlantPhotoEntity
    }
}

private const val TAG = "{$APP_TAG}.PlantPhotoRepositoryImpl"