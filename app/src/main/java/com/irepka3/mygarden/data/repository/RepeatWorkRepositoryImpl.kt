package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.RepeatWorkEntity
import com.irepka3.mygarden.data.database.RepeatWorkWithScheduele
import com.irepka3.mygarden.data.database.ScheduleEntity
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Schedule
import com.irepka3.mygarden.domain.model.WorkStatus
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

    init {
        Log.d(TAG, "init called")
    }

    override fun getAllActive(): List<RepeatWork> {
        Log.d(TAG, "getAllActive() called")
        return database.repeatWorkDao.getAllActive()?.map { it.toDomain() } ?: emptyList()
    }

    override fun getAllActiveByMonth(month: Int): List<RepeatWork> {
        Log.d(TAG, "getAllActiveByPeriod() called with: month = $month")
        return database.repeatWorkDao.getAllActive()
            ?.filter {
                // фильтруем тольк те schedule, у которых есть месяц совпадающиц с заданным или месяц не указан
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
        val schedulesUpdate = repeatWork.schedules.filter { it.scheduleId != null }
        if (schedulesUpdate.isNotEmpty()) {
            database.scheduleDao.update(schedulesUpdate.map { it.toEntity() })
        }

        val schedulesInsert = repeatWork.schedules.filter { it.scheduleId == null }
        if (schedulesInsert.isNotEmpty()) {
            database.scheduleDao.insert(schedulesInsert.map { it.toEntity() })
        }

    }

    override fun deleteRepeatWork(repeatWork: RepeatWork) {
        Log.d(TAG, "deleteRepeatWork() called with: repeatWork = $repeatWork")
        database.repeatWorkDao.delete(repeatWork.toEntity())
    }

    /**
     * Трансформирует модель доменного слоя [Schedule] в запись таблицы [ScheduleEntity]
     */
    private fun Schedule.toEntity(): ScheduleEntity {
        val schedule = ScheduleEntity()
        schedule.scheduleId = this.scheduleId ?: 0L
        schedule.repeatWorkId = this.repeatWorkId ?: 0L
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

    /**
     * Трансформирует модель доменного слоя [RepeatWork] в запись таблицы [RepeatWorkEntity]
     */
    private fun RepeatWork.toEntity(): RepeatWorkEntity {
        val repeatWork = RepeatWorkEntity()
        repeatWork.repeatWorkId = this.repeatWorkId ?: 0L
        repeatWork.name = this.name
        repeatWork.description = this.description
        repeatWork.notificationDay = this.notificationDay
        repeatWork.notificationHour = this.notificationHour
        repeatWork.notificationMinute = this.notificationMinute
        repeatWork.noNotification = this.noNotification
        repeatWork.status = this.status.value

        return repeatWork
    }

    /**
     * Трансформирует запись таблицы [RepeatWorkWithScheduele] в модель доменного слоя[RepeatWork]
     */
    private fun RepeatWorkWithScheduele.toDomain(): RepeatWork {
        return RepeatWork(
            repeatWorkId = this.repeatWork.repeatWorkId,
            name = this.repeatWork.name,
            description = this.repeatWork.description,
            notificationDay = this.repeatWork.notificationDay,
            notificationHour = this.repeatWork.notificationHour,
            notificationMinute = this.repeatWork.notificationMinute,
            noNotification = this.repeatWork.noNotification,
            status = WorkStatus.fromValue(this.repeatWork.status),
            schedules = this.schedule.map { it.toDomain() }
        )
    }
}

private const val TAG = "${APP_TAG}.RepeatWorkRepositoryImpl"