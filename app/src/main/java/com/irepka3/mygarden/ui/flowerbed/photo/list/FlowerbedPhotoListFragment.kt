package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.os.Bundle
import android.util.Log
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.util.Const.APP_TAG


/**
 * Фрагмент для отображения списка фотографий клумбы
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoListFragment: BasePhotoListFragment() {
    private var flowerbedId: Long = 0L

    companion object {
        /**
         * Создает фрагмент со списком фотографий клумбы
         * @param flowerbedId идентификатор клумбы         *
         */
        fun newInstance(flowerbedId: Long): FlowerbedPhotoListFragment {
            return FlowerbedPhotoListFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                }
            }
        }
    }

    override fun readArguments() {
        Log.d(TAG, "readArguments() called, id = $id")
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw Exception("Incorrect arguments: flowerbedId = $flowerbedId")
        Log.d(TAG, "readArguments() success, id = $id")
    }

    override fun createViewModel(): BasePhotoViewModel {
        Log.d(TAG, "createViewModel() called")
        return FlowerbedPhotoListViewModel(
            flowerbedId,
            dagger().getFileInteractor(),
            dagger().getFlowerbedPhotoInteractor()
        )
    }

    override fun onPhotoClick(photoPosition: Int) {
        (requireActivity() as MainActivityIntf).showFlowerbedPhoto(flowerbedId, photoPosition)
    }
}


private const val TAG = "${APP_TAG}.FlowerbedPhotoListFragment"
private const val FLOWERBED_ID = "flowerbedId"

