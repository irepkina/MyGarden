package com.irepka3.mygarden.ui.flowerbed.plants.photo.list

import android.os.Bundle
import android.util.Log
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.ui.flowerbed.photo.list.BasePhotoListFragment
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.util.Const.APP_TAG


/**
 * Фрагмент для отображения списка фотографий растений
 *
 * Created by i.repkina on 04.11.2021.
 */
class PlantPhotoListFragment: BasePhotoListFragment() {
    private var flowerbedId: Long = 0L
    private var plantId: Long = 0L

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
    }

    override fun createViewModel(): BasePhotoViewModel {
        return PlantPhotoListViewModel(
            flowerbedId = flowerbedId,
            plantId = plantId,
            dagger().getFileInteractor(),
            dagger().getPlantPhotoInteractor()
        )
    }

    override fun onPhotoClick(photoPosition: Int) {
        (requireActivity() as MainActivityIntf).showPlantPhoto(
            flowerbedId = flowerbedId,
            plantId = plantId,
            photoPosition
        )
    }

    companion object {
        /**
         * Создает фрагмент со списком фотографий растения
         * * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun newInstance(flowerbedId: Long, plantId: Long): PlantPhotoListFragment {
            return PlantPhotoListFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putLong(PLANT_ID, plantId)
                }
            }
        }
    }
}


private const val TAG = "${APP_TAG}.FlowerbedPhotoListFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val PLANT_ID = "plantId"


