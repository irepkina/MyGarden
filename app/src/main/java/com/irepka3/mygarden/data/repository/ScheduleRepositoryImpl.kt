package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.ScheduleEntity
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

    init {
        Log.d(TAG, "init called")
    }

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

    /**
     * Трансформирует модель доменного слоя [Schedule] в запись таблицы [ScheduleEntity]
     */
    private fun Schedule.toEntity(): ScheduleEntity {
        val schedule = ScheduleEntity()
        schedule.repeatWorkId = this.repeatWorkId ?: 0
        schedule.month = this.month
        schedule.week = this.week
        schedule.monday = this.monday
        schedule.tuesday = this.tuesday
        schedule.wednesday = this.wednesday
        schedule.thursday = this.thursday
        schedule.friday = this.friday
        schedule.saturday = this.saturday
        schedule.sunday = this.sunday

        return schedule

    }
}

/**
 * Трансформирует запись таблицы [ScheduleEntity] в модель доменного слоя[Schedule]
 */
fun ScheduleEntity.toDomain(): Schedule {
    return Schedule(
        scheduleId = this.scheduleId,
        repeatWorkId = this.repeatWorkId,
        month = this.month,
        week = this.week,
        monday = this.monday,
        tuesday = this.tuesday,
        wednesday = this.wednesday,
        thursday = this.thursday,
        friday = this.friday,
        saturday = this.saturday,
        sunday = this.sunday
    )
}

private const val TAG = "${APP_TAG}.RepeatWorkRepositoryImpl"