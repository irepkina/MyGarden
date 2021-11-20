package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.WorkEntity
import com.irepka3.mygarden.data.database.WorkWithRepeatWork
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
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

    init {
        Log.d(TAG, "init called")
    }

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

    /**
     * Трансформирует модель доменного слоя [Work] в запись таблицы [WorkEntity]
     */
    private fun Work.toEntity(): WorkEntity {
        val work = WorkEntity()
        work.workId = this.workId ?: 0
        work.repeatWorkId = this.repeatWork?.repeatWorkId
        work.name = this.name
        work.description = this.description
        work.dateDone = this.dateDone
        work.datePlan = this.datePlan
        work.status = this.status.value
        work.notificationDay = this.notificationDay
        work.notificationHour = this.notificationHour
        work.notificationMinute = this.notificationMinute
        return work

    }

    /**
     * Трансформирует запись таблицы [WorkEntity] в модель доменного слоя[Work]
     */
    private fun WorkWithRepeatWork.toDomain(): Work {
        val repeatWorkEntity = this.repeatWork

        val repeatWorkDomain = if (repeatWorkEntity != null) {
            RepeatWork(
                repeatWorkId = repeatWorkEntity.repeatWorkId,
                name = repeatWorkEntity.name,
                description = repeatWorkEntity.description,
                notificationDay = repeatWorkEntity.notificationDay,
                notificationHour = repeatWorkEntity.notificationHour,
                notificationMinute = repeatWorkEntity.notificationMinute,
                noNotification = repeatWorkEntity.noNotification,
                status = WorkStatus.fromValue(repeatWorkEntity.status),
                schedules = schedule?.map { it.toDomain() } ?: emptyList()
            )
        } else null

        return Work(
            workId = this.work.workId,
            repeatWork = repeatWorkDomain,
            name = this.work.name,
            description = this.work.description,
            datePlan = this.work.datePlan,
            dateDone = this.work.dateDone,
            status = WorkStatus.fromValue(this.work.status),
            notificationDay = this.work.notificationDay,
            notificationHour = this.work.notificationHour,
            notificationMinute = this.work.notificationMinute,
            noNotification = this.work.noNotification
        )
    }
}

private const val TAG = "${APP_TAG}.WorkRepositoryImpl"
