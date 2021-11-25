package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.RepeatWorkEntity
import com.irepka3.mygarden.data.database.RepeatWorkWithScheduele
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.WorkStatus


/**
 * Трансформирует модель доменного слоя [RepeatWork] в запись таблицы [RepeatWorkEntity]
 */
fun RepeatWork.toEntity(): RepeatWorkEntity {
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
fun RepeatWorkWithScheduele.toDomain(): RepeatWork {
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