package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.repository.WorkRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Репозиторий для работы с работами
 * @param database база данных
 *
 * Created by i.repkina on 10.11.2021.
 */

class WorkRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : WorkRepository {

    override fun getAll(dateFrom: Long, dateTo: Long): List<Work> {
        Log.d(TAG, "getAll() called")
        return database.workDao.getAll(dateFrom, dateTo)?.map { it.toDomain() } ?: emptyList()
    }

    override fun getById(workId: Long): Work? {
        Log.d(TAG, "getById() called")
        return database.workDao.getById(workId)?.toDomain()
    }

    override fun insertWork(work: Work): Long {
        Log.d(TAG, "insertWork() called with: work = $work")
        return database.workDao.insert(work.toEntity()).also { workId ->
            Log.d(TAG, "insertRepeatWork() return workId = $workId")
        }
    }

    override fun updateWork(work: Work) {
        Log.d(TAG, "updateWork() called with: work = $work")
        database.workDao.update(work.toEntity())
    }

    override fun deleteWork(work: Work) {
        Log.d(TAG, "deleteWork() called with: work = $work")
        database.workDao.delete(work.toEntity())
    }
}

private const val TAG = "${APP_TAG}.WorkRepositoryImpl"
