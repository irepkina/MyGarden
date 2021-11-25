package com.irepka3.mygarden.ui.flowerbed.photo.photo

import android.os.Bundle
import android.util.Log
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.ui.flowerbed.FlowerbedFragment
import com.irepka3.mygarden.ui.flowerbed.photo.list.FlowerbedPhotoListViewModel
import com.irepka3.mygarden.ui.photo.BasePhotoFragment
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.util.Const


/**
 * Фрагмент для отображения фотографии
 *
 * Created by i.repkina on 06.11.2021.
 */
class FragmentFlowerbedPhoto : BasePhotoFragment() {
    private var flowerbedId: Long = 0L
    private var photoPosition: Int = -1

    override fun readArguments() {
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw IllegalStateException("Incorrect arguments: flowerbedId = $flowerbedId")
        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId")
        photoPosition = arguments?.getInt(PHOTO_POSITION) ?: 1

        if (photoPosition < 0)
            throw IllegalStateException("Incorrect arguments: photoPosition = $photoPosition")
        Log.d(TAG, "readArguments() success, photoPosition = $photoPosition")

        caption = FlowerbedFragment.currentFlowerbedName
    }

    override fun scrollPosition(): Int {
        return photoPosition
    }

    override fun createViewModel(): BasePhotoViewModel {
        return FlowerbedPhotoListViewModel(
            flowerbedId,
            dagger().getFileInteractor(),
            dagger().getFlowerbedPhotoInteractor()
        )
    }

    companion object {
        /**
         * Создает фрагмент со списком фотографий клумбы
         * @param flowerbedId идентификатор клумбы
         * @param photoPosition номер фото в списке фото клумб
         */
        fun newInstance(flowerbedId: Long, photoPosition: Int): FragmentFlowerbedPhoto {
            return FragmentFlowerbedPhoto().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putInt(PHOTO_POSITION, photoPosition)
                }
            }
        }
    }
}


private const val TAG = "${Const.APP_TAG}.FragmentFlowerbedPhoto"
private const val FLOWERBED_ID = "flowerbedId"
private const val PHOTO_POSITION = "photoPosition"