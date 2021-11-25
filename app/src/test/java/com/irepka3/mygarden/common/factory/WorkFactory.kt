package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.RepeatWorkEntity
import com.irepka3.mygarden.data.database.ScheduleEntity
import com.irepka3.mygarden.data.database.WorkEntity
import com.irepka3.mygarden.data.database.WorkWithRepeatWork
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus

/**
 * Класс для создания экзепляров работы
 *
 * Created by i.repkina on 21.11.2021.
 */
class WorkFactory {
    companion object {
        /**
         * Создает экземпляр класса [Work]
         * @param workId идентификатор работы
         * @param datePlan плановая дата
         * @param repeatWork повторяющаяся работа [RepeatWork]
         */
        fun createWork(workId: Long?, datePlan: Long?, repeatWork: RepeatWork?): Work {
            return Work(
                workId = workId,
                name = "name",
                description = "description",
                status = WorkStatus.Plan,
                datePlan = datePlan,
                repeatWork = repeatWork
            )
        }

        /**
         * Создает экземпляр класса [WorkEntity]
         * @param workId идентификатор работы
         * @param repeatWorkId идентификатор повторяющейся работы
         * @param datePlan плановая дата
         */
        fun createWorkEntity(workId: Long?, repeatWorkId: Long?, datePlan: Long?): WorkEntity {
            return WorkEntity(
                workId = workId ?: 0L,
                repeatWorkId = repeatWorkId,
                name = "name",
                description = "description",
                status = 0,
                datePlan = datePlan
            )
        }

        /**
         * Создает экземпляр класса [WorkWithRepeatWork]
         * @param workId идентификатор работы
         * @param datePlan плановая дата
         * @param repeatWork повторяющаяся работа [RepeatWorkEntity]
         * @param schedules список расписаний [ScheduleEntity]
         */
        fun createWorkWithRepeatWork(
            workId: Long?,
            datePlan: Long?,
            repeatWork: RepeatWorkEntity?,
            schedules: List<ScheduleEntity>?
        ): WorkWithRepeatWork {
            return WorkWithRepeatWork(
                work = createWorkEntity(
                    workId = workId,
                    repeatWorkId = repeatWork?.repeatWorkId ?: 0L,
                    datePlan = datePlan
                ),
                repeatWork = repeatWork,
                schedule = schedules
            )
        }
    }
}