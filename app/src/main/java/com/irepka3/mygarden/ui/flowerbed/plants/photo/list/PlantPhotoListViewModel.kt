package com.irepka3.mygarden.ui.flowerbed.plants.photo.list

import android.util.Log
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.PlantPhotoInteractor
import com.irepka3.mygarden.domain.model.PlantPhoto
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.ui.photo.model.Photo
import com.irepka3.mygarden.util.Const
import java.io.File

/**
 * View-модель для фрагмента списка фотографий растений
 * @param plantId идентификатор растения
 * @param flowerbedId идентификатор клумбы
 * @param fileInteractor интерактор доменного слоя для работы с фотографиями [FileInteractor]
 * @param plantPhotoInteractor интерактор доменного слоя для работы с фотографиями растения [PlantPhotoInteractor]
 *
 * Created by i.repkina on 07.11.2021.
 */

class PlantPhotoListViewModel(
    private val flowerbedId: Long,
    private val plantId: Long,
    fileInteractor: FileInteractor,
    private val plantPhotoInteractor: PlantPhotoInteractor
): BasePhotoViewModel(fileInteractor) {

    override fun doLoadData(): List<Photo> {
        return plantPhotoInteractor.getAllByPlantId(plantId)
        ?.map { Photo(photoId = it.plantPhotoId, uri = it.uri, selected = it.selected) } ?: emptyList()
    }

    override fun doInsert(uri: String) {
        val plantPhoto = PlantPhoto(plantId = plantId, plantPhotoId = null, uri = uri)
        plantPhotoInteractor.insertPlantPhoto(plantPhoto)
    }

    override fun doDelete(photoList: List<Photo>) {
        plantPhotoInteractor.deletePlantPhoto(photoList.map {
            PlantPhoto(plantId = plantId, plantPhotoId = it.photoId, uri = it.uri, selected = it.selected)
        })
    }

    override fun getPhotoDir(): File {
        return plantPhotoInteractor.getPlantDir(flowerbedId = flowerbedId, plantId = plantId)
    }

    override fun doSelected(photoId: Long) {
        Log.d(TAG, "doSelected() called with: photoId = $photoId")
        plantPhotoInteractor.updateSelectedPhoto(plantId = plantId, plantPhotoId = photoId)
    }
}

private const val TAG = "{${Const.APP_TAG}}.PlantPhotoListViewModel"