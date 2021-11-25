package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.ScheduleEntity
import com.irepka3.mygarden.domain.model.Schedule


/**
 * Трансформирует модель доменного слоя [Schedule] в запись таблицы [ScheduleEntity]
 */
fun Schedule.toEntity(): ScheduleEntity {
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