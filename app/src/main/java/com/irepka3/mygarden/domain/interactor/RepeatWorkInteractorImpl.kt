package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.repository.RepeatWorkRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Реализация интерактора для работы с повторяющимися работами
 * @param repeatWorkRepository интерфейс репозитория дата-слоя
 *
 * Created by i.repkina on 01.11.2021.
 */
class RepeatWorkInteractorImpl @Inject constructor(
    private val repeatWorkRepository: RepeatWorkRepository
) : RepeatWorkInteractor {

    override fun getAllActive(): List<RepeatWork> {
        return repeatWorkRepository.getAllActive()
    }

    override fun getAllActiveByMonth(month: Int): List<RepeatWork> {
        return repeatWorkRepository.getAllActiveByMonth(month)
    }

    override fun getById(repeatWorkId: Long): RepeatWork {
        return repeatWorkRepository.getById(repeatWorkId)
            ?: throw IllegalStateException("RepeatWork not found by id = $repeatWorkId")
    }

    override fun insertRepeatWork(repeatWork: RepeatWork): Long {
        return repeatWorkRepository.insertRepeatWork(repeatWork)
    }

    override fun updateRepeatWork(repeatWork: RepeatWork) {
        repeatWorkRepository.updateRepeatWork(repeatWork)
    }

    override fun deleteRepeatWork(repeatWork: RepeatWork) {
        if (repeatWork.repeatWorkId == null) {
            throw IllegalStateException("Invalid repeatWorkId, repeatWorkId is null")
        }
        repeatWorkRepository.deleteRepeatWork(repeatWork)
    }
}

private const val TAG = "${APP_TAG}.RepeatWorkInteractorImpl"