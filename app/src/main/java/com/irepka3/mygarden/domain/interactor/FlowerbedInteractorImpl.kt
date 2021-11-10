package com.irepka3.mygarden.domain.interactor

import android.util.Log
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import java.lang.Exception
import javax.inject.Inject

/**
 * Реализация интерактора для работы с клумбами
 * @param flowerbedRepository интерфейс репозитория дата-слоя
 *
 * Created by i.repkina on 01.11.2021.
 */
class FlowerbedInteractorImpl
@Inject constructor(
    private val flowerbedRepository: FlowerbedRepository,
    private val dirRepository: DirRepository
    ) : FlowerbedInteractor {

    override fun getFlowerbedAll(): List<Flowerbed> {
        return flowerbedRepository.getFlowerbedAll()
    }

    override fun getFlowerbed(flowerbedId: Long): Flowerbed? {
       return flowerbedRepository.getFlowerbed(flowerbedId)
    }

    override fun insertFlowerbed(flowerbed: Flowerbed): Long {
        return flowerbedRepository.insertFlowerbed(flowerbed)
    }

    override fun updateFlowerbed(flowerbed: Flowerbed) {
        flowerbedRepository.updateFlowerbed(flowerbed)
    }

    override fun deleteFlowerbed(flowerbed: Flowerbed) {
        if (flowerbed.flowerbedId == null)
            throw Exception("Invalid flowerbedId, flowerbedId is null")
        val file = dirRepository.getFlowerbedDir(flowerbed.flowerbedId)
        if (file.exists()) {
            file.deleteRecursively()
        }
        flowerbedRepository.deleteFlowerbed(flowerbed)
    }
}

private const val TAG = "${APP_TAG}.FlowerbedInteractorImpl"