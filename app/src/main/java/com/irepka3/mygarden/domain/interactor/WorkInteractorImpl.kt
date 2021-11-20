package com.irepka3.mygarden.domain.interactor


import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.repository.WorkRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Реализация интерактора для работы с работами
 * @param workRepository интерфейс репозитория дата-слоя
 *
 * Created by i.repkina on 01.11.2021.
 */
class WorkInteractorImpl @Inject constructor(
    private val workRepository: WorkRepository
) : WorkInteractor {
    override fun getAll(dateFrom: Long, dateTo: Long): List<Work> {
        return workRepository.getAll(dateFrom, dateTo)
    }

    override fun getById(workId: Long): Work {
        return workRepository.getById(workId)
            ?: throw IllegalStateException("Work not found by id = $workId")
    }

    override fun insertWork(work: Work): Long {
        return workRepository.insertWork(work)
    }

    override fun updateWork(work: Work) {
        workRepository.updateWork(work)
    }

    override fun deleteWork(work: Work) {
        if (work.workId == null) {
            throw IllegalStateException("Invalid workId, workId is null")
        }
        workRepository.deleteWork(work)
    }
}

private const val TAG = "${APP_TAG}.WorkInteractorImpl"