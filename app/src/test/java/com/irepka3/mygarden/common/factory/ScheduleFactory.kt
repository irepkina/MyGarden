package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.ScheduleEntity
import com.irepka3.mygarden.domain.model.Schedule

/**
 * Создание экземпляров расписаний
 *
 * Created by i.repkina on 22.11.2021.
 */
class ScheduleFactory {
    companion object {
        /**
         * Создание расписания [Schedule]
         * @param scheduleId идентификатор расписания
         * @param repeatWorkId идентификатор повторяющейся работы
         */
        fun createSchedule(scheduleId: Long?, repeatWorkId: Long?): Schedule {
            return Schedule(scheduleId = scheduleId, repeatWorkId = repeatWorkId)
        }

        /**
         * Создание расписания [ScheduleEntity]
         * @param scheduleId идентификатор расписания
         * @param repeatWorkId идентификатор повторяющейся работы
         */
        fun createScheduleEntity(scheduleId: Long?, repeatWorkId: Long?): ScheduleEntity {
            return ScheduleEntity(scheduleId = scheduleId ?: 0L, repeatWorkId = repeatWorkId ?: 0L)

        }

        /**
         * Создание расписания [ScheduleEntity] с парметрами месяц и дни недели
         * @param scheduleId идентификатор расписания
         * @param repeatWorkId идентификатор повторяющейся работы
         * @param month номер месяцва
         * @param monday понедельник
         * @param week номер недели
         * @param tuesday вторник
         * @param wednesday среда
         * @param thursday четверг
         * @param friday пятница
         * @param saturday суббота
         * @param sunday воскресенье
         */
        fun createScheduleEntityWithMonthDay(
            scheduleId: Long?,
            repeatWorkId: Long?,
            month: Int? = 0,
            week: Int? = 0,
            monday: Boolean = false,
            tuesday: Boolean = false,
            wednesday: Boolean = false,
            thursday: Boolean = false,
            friday: Boolean = false,
            saturday: Boolean = false,
            sunday: Boolean = false
        ): ScheduleEntity {
            return ScheduleEntity(
                scheduleId = scheduleId ?: 0L,
                repeatWorkId = repeatWorkId ?: 0L,
                month = month,
                week = week,
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday,
                friday = friday,
                saturday = saturday,
                sunday = sunday
            )
        }

        /**
         * Создание расписания [Schedule] с парметрами месяц и дни недели
         * @param scheduleId идентификатор расписания
         * @param repeatWorkId идентификатор повторяющейся работы
         * @param month номер месяца
         * @param week номер недели
         * @param monday понедельник
         * @param tuesday вторник
         * @param wednesday среда
         * @param thursday четверг
         * @param friday пятница
         * @param saturday суббота
         * @param sunday воскресенье
         */
        fun createScheduleWithMonthDay(
            scheduleId: Long?,
            repeatWorkId: Long?,
            month: Int? = 0,
            week: Int? = 0,
            monday: Boolean = false,
            tuesday: Boolean = false,
            wednesday: Boolean = false,
            thursday: Boolean = false,
            friday: Boolean = false,
            saturday: Boolean = false,
            sunday: Boolean = false
        ): Schedule {
            return Schedule(
                scheduleId = scheduleId ?: 0L,
                repeatWorkId = repeatWorkId ?: 0L,
                month = month,
                week = week,
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday,
                friday = friday,
                saturday = saturday,
                sunday = sunday
            )
        }
    }
}