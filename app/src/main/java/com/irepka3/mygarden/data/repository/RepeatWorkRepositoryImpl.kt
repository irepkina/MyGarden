package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.repository.RepeatWorkRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Репозиторий для работы с повторяющимися работами
 * @param database база данных
 *
 * Created by i.repkina on 10.11.2021.
 */

class RepeatWorkRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : RepeatWorkRepository {

    override fun getAllActive(): List<RepeatWork> {
        Log.d(TAG, "getAllActive() called")
        return database.repeatWorkDao.getAllActive()?.map { it.toDomain() } ?: emptyList()
    }

    override fun getAllActiveByMonth(month: Int): List<RepeatWork> {
        Log.d(TAG, "getAllActiveByPeriod() called with: month = $month")
        return database.repeatWorkDao.getAllActive()
            ?.filter {
                // фильтруем только те schedule, у которых есть месяц совпадающий с заданным или месяц не указан
                it.schedule.filter { it.month == null || it.month == month }.isNotEmpty()
            }
            ?.map { it.toDomain() } ?: emptyList()
    }

    override fun getById(repeatWorkId: Long): RepeatWork? {
        Log.d(TAG, "getById() called with: id = $repeatWorkId")
        return database.repeatWorkDao.getById(repeatWorkId)?.toDomain()
    }

    override fun insertRepeatWork(repeatWork: RepeatWork): Long {
        Log.d(TAG, "insertRepeatWork() called with: repeatWork = $repeatWork")
        val repeatWorkId =
            database.repeatWorkDao.insert(repeatWork.toEntity()).also { repeatWorkId ->
                Log.d(TAG, "insertRepeatWork() return repeatWorkId = $repeatWorkId")
            }

        repeatWork.schedules.forEach { element -> element.repeatWorkId = repeatWorkId }
        if (repeatWork.schedules.isNotEmpty()) {
            database.scheduleDao.insert(repeatWork.schedules.map { it.toEntity() })
        }

        return repeatWorkId
    }

    override fun updateRepeatWork(repeatWork: RepeatWork) {
        Log.d(TAG, "updateRepeatWork() called with: repeatWork = $repeatWork")
        database.repeatWorkDao.update(repeatWork.toEntity())

        database.scheduleDao.deleteAll(repeatWork.repeatWorkId ?: 0L)

        if (repeatWork.schedules.isNotEmpty()) {
            database.scheduleDao.insert(repeatWork.schedules.map { it.toEntity() })
        }
    }

    override fun deleteRepeatWork(repeatWork: RepeatWork) {
        Log.d(TAG, "deleteRepeatWork() called with: repeatWork = $repeatWork")
        database.repeatWorkDao.delete(repeatWork.toEntity())
    }
}

private const val TAG = "${APP_TAG}.RepeatWorkRepositoryImpl"