package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
import com.irepka3.mygarden.domain.model.Schedule
import com.irepka3.mygarden.domain.repository.ScheduleRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject

/**
 * Репозиторий для работы с расписанием
 * @param database база данных
 *
 * Created by i.repkina on 10.11.2021.
 */

class ScheduleRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : ScheduleRepository {

    override fun getByRepeatWorkId(repeatWorkId: Long): List<Schedule> {
        Log.d(TAG, "getByRepeatWorkId() called")
        return database.scheduleDao.getByRepeatWorkId(repeatWorkId)?.map { it.toDomain() }
            ?: emptyList()
    }

    override fun getById(scheduleId: Long): Schedule {
        Log.d(TAG, "getById() called with: id = $scheduleId")
        return database.scheduleDao.getById(scheduleId).toDomain()
    }

    override fun insert(schedules: List<Schedule>): List<Long> {
        Log.d(TAG, "insertSchedule() called with: schedule = $schedules")
        return database.scheduleDao.insert(schedules.map { it.toEntity() })
    }

    override fun update(schedules: List<Schedule>) {
        Log.d(TAG, "update() called with: schedule = $schedules")
        database.scheduleDao.update(schedules.map { it.toEntity() })
    }

    override fun deleteAll(repeatWorkId: Long) {
        Log.d(TAG, "deleteAll() called with: repeatWorkId = $repeatWorkId")
        database.scheduleDao.deleteAll(repeatWorkId)
    }
}

private const val TAG = "${APP_TAG}.RepeatWorkRepositoryImpl"