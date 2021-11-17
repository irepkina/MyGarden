package com.irepka3.mygarden.ui.flowerbed.plants.photo.photo

import android.os.Bundle
import android.util.Log
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.ui.flowerbed.photo.photo.BasePhotoFragment
import com.irepka3.mygarden.ui.flowerbed.plants.photo.list.PlantPhotoListViewModel
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.util.Const


/**
 * Фрагмент для отображения фотографии
 *
 * Created by i.repkina on 07.11.2021.
 */
class FragmentPlantPhoto: BasePhotoFragment() {
    private var flowerbedId: Long = 0
    private var plantId: Long = 0L
    private var photoPosition: Int = -1

    override fun readArguments() {
        Log.d(TAG, "readArguments() called")
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw IllegalStateException("Incorrect arguments: flowerbedId = $flowerbedId")
        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId")

        plantId = arguments?.getLong(PLANT_ID) ?: 0L
        if (plantId == 0L)
            throw IllegalStateException("Incorrect arguments: plantId = $plantId")
        Log.d(TAG, "readArguments() success, plantId = $plantId")

        photoPosition = arguments?.getInt(PHOTO_POSITION) ?: 1
        if (photoPosition < 0)
            throw IllegalStateException("Incorrect arguments: photoPosition = $photoPosition")
        Log.d(TAG, "readArguments() success, photoPosition = $photoPosition")
    }

    override fun scrollPosition(): Int {
        return photoPosition
    }

    override fun createViewModel(): BasePhotoViewModel {
        return PlantPhotoListViewModel(
            flowerbedId = flowerbedId,
            plantId = plantId,
            dagger().getFileInteractor(),
            dagger().getPlantPhotoInteractor()
        )
    }

    companion object {
        /**
         * Создает фрагмент со списком фотографий растения
         * @param plantId идентификатор растения
         * @param flowerbedId идентификатор клумбы
         * @param photoPosition номер фото в списке фото клумб
         */
        fun newInstance(flowerbedId: Long, plantId: Long, photoPosition: Int): FragmentPlantPhoto {
            return FragmentPlantPhoto().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putLong(PLANT_ID, plantId)
                    putInt(PHOTO_POSITION, photoPosition)
                }
            }
        }
    }
}


private const val TAG = "${Const.APP_TAG}.FragmentPlantPhoto"
private const val FLOWERBED_ID = "flowerbedId"
private const val PLANT_ID = "plantId"
private const val PHOTO_POSITION = "photoPosition"