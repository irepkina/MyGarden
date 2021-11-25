package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.common.factory.RepeatWorkFactory
import com.irepka3.mygarden.common.factory.ScheduleFactory
import com.irepka3.mygarden.common.factory.WorkFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса WorkRepositoryImpl
 * Created by i.repkina on 22.11.2021.
 */
class WorkRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = WorkRepositoryImpl(database)

    @Test
    fun getAll() {
        // prepare
        val dateFrom = 1L
        val dateTo = 1L
        val databaseResult = listOf(
            WorkFactory.createWorkWithRepeatWork(
                workId = 1,
                datePlan = null,
                repeatWork = RepeatWorkFactory.createRepeatWorkEntity(repeatWorkId = 1),
                schedules = listOf(
                    ScheduleFactory.createScheduleEntity(scheduleId = 1, repeatWorkId = 1),
                    ScheduleFactory.createScheduleEntity(scheduleId = 2, repeatWorkId = 1)
                )
            )
        )
        val expected = listOf(
            WorkFactory.createWork(
                workId = 1,
                datePlan = null,
                repeatWork = RepeatWorkFactory.createRepeatWork(1)
            )
        )
        every { database.workDao.getAll(dateFrom, dateTo) } returns databaseResult

        // do
        val actual = underTest.getAll(dateFrom, dateTo)

        // check
        verify { database.workDao.getAll(dateFrom, dateTo) }
        assertEquals(expected, actual)
    }

    @Test
    fun getById() {
        // prepare
        val workId = 1L

        val databaseResult =
            WorkFactory.createWorkWithRepeatWork(
                workId = 1,
                datePlan = null,
                repeatWork = RepeatWorkFactory.createRepeatWorkEntity(repeatWorkId = 1),
                schedules = listOf(
                    ScheduleFactory.createScheduleEntity(scheduleId = 1, repeatWorkId = 1),
                    ScheduleFactory.createScheduleEntity(scheduleId = 2, repeatWorkId = 1)
                )
            )
        val expected =
            WorkFactory.createWork(
                workId = 1,
                datePlan = null,
                repeatWork = RepeatWorkFactory.createRepeatWork(1)
            )
        every { database.workDao.getById(workId) } returns databaseResult

        // do
        val actual = underTest.getById(workId)

        // check
        verify { database.workDao.getById(workId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertWork() {
        // prepare
        val expected = 1L
        val work = WorkFactory.createWork(null, null, null)

        val workEntity =
            WorkFactory.createWorkEntity(null, null, null)

        every { database.workDao.insert(workEntity) } returns expected

        // do
        val actual = underTest.insertWork(work)

        // check
        verify { database.workDao.insert(workEntity) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateWork() {
        // prepare
        val work = WorkFactory.createWork(null, null, null)

        val workEntity =
            WorkFactory.createWorkEntity(null, null, null)

        every { database.workDao.update(workEntity) } returns Unit

        // do
        underTest.updateWork(work)

        // check
        verify { database.workDao.update(workEntity) }
    }

    @Test
    fun deleteWork() {
        // prepare
        val work = WorkFactory.createWork(null, null, null)

        val workEntity =
            WorkFactory.createWorkEntity(null, null, null)

        every { database.workDao.delete(workEntity) } returns Unit

        // do
        underTest.deleteWork(work)

        // check
        verify { database.workDao.delete(workEntity) }
    }
}