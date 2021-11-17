package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.Schedule

/**
 * Интерфейс доменного слоя для репозитория расписания работ.
 *
 * Created by i.repkina on 10.11.2021.
 */
interface ScheduleRepository {
    // Получить список всех расписаний повторяющейся работы
    fun getByRepeatWorkId(repeatWorkId: Long): List<Schedule>

    // Добавить расписание
    fun insert(schedules: List<Schedule>): List<Long>

    // Изменить данные расписания
    fun update(schedules: List<Schedule>)

    // Удалить расписание
    fun delete(schedule: Schedule)

    // Получить расписание по идентификатору
    fun getById(scheduleId: Long): Schedule?
}