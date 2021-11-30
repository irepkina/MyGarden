package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.util.Log
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.ui.photo.model.Photo
import com.irepka3.mygarden.util.Const.APP_TAG
import java.io.File

/**
 * View-модель для фрагмента списка фотографий клумбы
 * @param flowerbedId идентификатор клумбы
 * @param fileInteractor интерактор доменного слоя для работы с фотографиями [FileInteractor]
 * @param flowerbedPhotoInteractor интерактор доменного слоя для работы с фотографиями клумбы [FlowerbedPhotoInteractor]
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoListViewModel(
    private val flowerbedId: Long,
    fileInteractor: FileInteractor,
    private val flowerbedPhotoInteractor: FlowerbedPhotoInteractor
) : BasePhotoViewModel(fileInteractor) {

    override fun doLoadData(): List<Photo> {
        return flowerbedPhotoInteractor.getAllByFlowerbedId(flowerbedId)
            ?.map { Photo(photoId = it.flowerbedPhotoId, uri = it.uri, selected = it.selected) }
            ?: emptyList()
    }

    override fun doInsert(uri: String) {
        val flowerbedPhoto =
            FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = null, uri, false)
        flowerbedPhotoInteractor.insertFlowerbedPhoto(flowerbedPhoto)
    }

    override fun doDelete(photoList: List<Photo>) {
        flowerbedPhotoInteractor.deleteFlowerbedPhoto(photoList.map {
            FlowerbedPhoto(
                flowerbedId = flowerbedId,
                flowerbedPhotoId = it.photoId,
                uri = it.uri,
                selected = it.selected
            )
        })
    }

    override fun getPhotoDir(): File {
        return flowerbedPhotoInteractor.getFlowerbedDir(flowerbedId)
    }

    override fun doSelected(photoId: Long) {
        Log.d(TAG, "doSelected() called with: photoId = $photoId")
        flowerbedPhotoInteractor.updateSelectedPhoto(
            flowerbedId = flowerbedId,
            flowerbedPhotoId = photoId
        )
    }
}

private const val TAG = "{$APP_TAG}.FlowerbedPhotoListViewModel"