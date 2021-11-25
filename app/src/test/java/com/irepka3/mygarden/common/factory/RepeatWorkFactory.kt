package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.RepeatWorkEntity
import com.irepka3.mygarden.data.database.RepeatWorkWithScheduele
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Schedule
import com.irepka3.mygarden.domain.model.WorkStatus

/**
 * Класс для создания экзепляров повторояющихся работ
 *
 * Created by i.repkina on 21.11.2021.
 */
class RepeatWorkFactory {
    companion object {
        /**
         * Создает экземпляр класса [RepeatWork]
         * @param repeatWorkId идентификатор повторяющейся работы
         */
        fun createRepeatWork(repeatWorkId: Long?): RepeatWork {
            val schedules = listOf(
                Schedule(scheduleId = 1, repeatWorkId = repeatWorkId),
                Schedule(scheduleId = 2, repeatWorkId = repeatWorkId)
            )
            return RepeatWork(
                repeatWorkId = repeatWorkId,
                name = "name",
                description = "description",
                status = WorkStatus.Plan,
                schedules = schedules
            )
        }

        /**
         * Создает экземпляр класса [RepeatWork] не сохраненной в базу ( с пустым repeatWorkId)
         */
        fun createNewRepeat(): RepeatWork {
            val schedules = listOf(
                Schedule(scheduleId = 1, repeatWorkId = null),
                Schedule(scheduleId = 2, repeatWorkId = null)
            )
            return RepeatWork(
                repeatWorkId = null,
                name = "name",
                description = "description",
                status = WorkStatus.Plan,
                schedules = schedules
            )
        }

        /**
         * Создает экземпляр класса [RepeatWorkEntity]
         * @param repeatWorkId идентификатор повторяющейся работы
         */
        fun createRepeatWorkEntity(repeatWorkId: Long?): RepeatWorkEntity {

            return RepeatWorkEntity(
                repeatWorkId = repeatWorkId ?: 0L,
                name = "name",
                description = "description",
                status = 0
            )
        }

        /**
         * Создает экземпляр класса [RepeatWorkWithScheduele]
         * @param repeatWorkId идентификатор повторяющейся работы
         */
        fun createRepeatWorkWithScheduele(repeatWorkId: Long?): RepeatWorkWithScheduele {

            return RepeatWorkWithScheduele(
                repeatWork = createRepeatWorkEntity(repeatWorkId),
                schedule = listOf(
                    ScheduleFactory.createScheduleEntity(1, repeatWorkId),
                    ScheduleFactory.createScheduleEntity(2, repeatWorkId)
                )
            )
        }

        /**
         * Создает экземпляр класса [RepeatWorkWithScheduele] с заданными параметрами расписания
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
        fun createRepeatWorkWithSchedueleMonthDay(
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
        ): RepeatWorkWithScheduele {

            return RepeatWorkWithScheduele(
                repeatWork = createRepeatWorkEntity(repeatWorkId),
                schedule = listOf(
                    ScheduleFactory.createScheduleEntityWithMonthDay(
                        scheduleId = 1,
                        repeatWorkId = repeatWorkId,
                        month = month,
                        week = week,
                        monday = monday,
                        tuesday = tuesday,
                        wednesday = wednesday,
                        thursday = thursday,
                        friday = friday,
                        saturday = saturday,
                        sunday = sunday
                    ),
                    ScheduleFactory.createScheduleEntityWithMonthDay(
                        scheduleId = 2,
                        repeatWorkId = repeatWorkId,
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
                )
            )
        }

        /**
         * Создает экземпляр класса [RepeatWork] с заданными параметрами расписания
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
        fun createRepeatWorkMonthDay(
            repeatWorkId: Long?,
            month: Int? = 0,
            week: Int?,
            monday: Boolean = false,
            tuesday: Boolean = false,
            wednesday: Boolean = false,
            thursday: Boolean = false,
            friday: Boolean = false,
            saturday: Boolean = false,
            sunday: Boolean = false
        ): RepeatWork {
            val schedules = listOf(
                Schedule(
                    scheduleId = 1,
                    repeatWorkId = repeatWorkId,
                    month = month,
                    week = week,
                    monday = monday,
                    tuesday = tuesday,
                    wednesday = wednesday,
                    thursday = thursday,
                    friday = friday,
                    saturday = saturday,
                    sunday = sunday
                ),
                Schedule(
                    scheduleId = 2,
                    repeatWorkId = repeatWorkId,
                    month = month,
                    monday = monday,
                    week = week,
                    tuesday = tuesday,
                    wednesday = wednesday,
                    thursday = thursday,
                    friday = friday,
                    saturday = saturday,
                    sunday = sunday
                )
            )
            return RepeatWork(
                repeatWorkId = repeatWorkId,
                name = "name",
                description = "description",
                status = WorkStatus.Plan,
                schedules = schedules
            )
        }

    }
}