package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.Schedule


/**
 * Интерактор для работы со списком расписаний
 *
 * Created by i.repkina on 10.11.2021.
 */
interface ScheduleInteractor {
    // Получение списка расписаний повторяющейся работы
    fun getScheduleByRepewtWorkIdAll(): List<Schedule>

    // Получение повторяющейся расписания по идентификатору
    fun getSchedule(scheduleId: Long): Schedule?

    // Добавление расписания
    fun insertSchedule(schedule: Schedule): Long

    // Обновление данных расписания
    fun updateSchedule(schedule: Schedule)

    // Удаление расписание
    fun deleteSchedule(schedule: Schedule)
}