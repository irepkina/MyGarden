package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.WorkEntity
import com.irepka3.mygarden.data.database.WorkWithRepeatWork
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus

/**
 * Трансформирует модель доменного слоя [Work] в запись таблицы [WorkEntity]
 */
fun Work.toEntity(): WorkEntity {
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
fun WorkWithRepeatWork.toDomain(): Work {
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