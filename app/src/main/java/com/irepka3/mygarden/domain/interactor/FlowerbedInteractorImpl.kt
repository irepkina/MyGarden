package com.irepka3.mygarden.domain.interactor

import android.util.Log
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Реализация интерактора для работы с клумбами
 * @param flowerbedRepository интерфейс репозитория дата-слоя
 *
 * Created by i.repkina on 01.11.2021.
 */
class FlowerbedInteractorImpl(private val flowerbedRepository: FlowerbedRepository): FlowerbedInteractor {
    override fun getFlowerbedAll(): List<Flowerbed> {
        return flowerbedRepository.getFlowerbedAll()
    }

    override fun getFlowerbed(flowerbedId: Long): Flowerbed? {
        Log.d(TAG, "getFlowerBed() called with: id = $flowerbedId")
       return flowerbedRepository.getFlowerbed(flowerbedId)
    }

    override fun insertFlowerbed(flowerbed: Flowerbed) {
        flowerbedRepository.insertFlowerbed(flowerbed)
    }

    override fun updateFlowerbed(flowerbed: Flowerbed) {
        flowerbedRepository.updateFlowerbed(flowerbed)
    }

    override fun deleteFlowerbed(flowerbed: Flowerbed) {
        flowerbedRepository.deleteFlowerbed(flowerbed)
    }
}

private const val TAG = "${APP_TAG}.FlowerbedInteractorImpl"