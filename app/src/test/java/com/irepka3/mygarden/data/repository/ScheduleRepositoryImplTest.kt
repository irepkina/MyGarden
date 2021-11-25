package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.common.factory.ScheduleFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса ScheduleRepositoryImpl
 *
 * Created by i.repkina on 22.11.2021.
 */
class ScheduleRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = ScheduleRepositoryImpl(database)

    @Test
    fun getByRepeatWorkId() {
        // prepare
        val repeatWorkId = 1L
        val databaseResult = listOf(
            ScheduleFactory.createScheduleEntity(scheduleId = 1, repeatWorkId = repeatWorkId),
            ScheduleFactory.createScheduleEntity(scheduleId = 2, repeatWorkId = repeatWorkId)

        )
        val expected = listOf(
            ScheduleFactory.createSchedule(scheduleId = 1, repeatWorkId = repeatWorkId),
            ScheduleFactory.createSchedule(scheduleId = 2, repeatWorkId = repeatWorkId)

        )
        every { database.scheduleDao.getByRepeatWorkId(repeatWorkId) } returns databaseResult

        // do
        val actual = underTest.getByRepeatWorkId(repeatWorkId)

        // check
        verify { database.scheduleDao.getByRepeatWorkId(repeatWorkId) }
        assertEquals(expected, actual)
    }

    @Test
    fun getById() {
        // prepare
        val scheduleId = 1L
        val repeatWorkId = 1L
        val repositoryResult = ScheduleFactory.createScheduleEntity(
            scheduleId = scheduleId,
            repeatWorkId = repeatWorkId
        )
        val expected =
            ScheduleFactory.createSchedule(scheduleId = scheduleId, repeatWorkId = repeatWorkId)

        every { database.scheduleDao.getById(scheduleId) } returns repositoryResult

        // do
        val actual = underTest.getById(repeatWorkId)

        // check
        verify { database.scheduleDao.getById(scheduleId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insert() {
        // prepare
        val repeatWorkId = 1L
        val scheduleEntityList = listOf(
            ScheduleFactory.createScheduleEntity(scheduleId = null, repeatWorkId = repeatWorkId),
            ScheduleFactory.createScheduleEntity(scheduleId = null, repeatWorkId = repeatWorkId)

        )
        val scheduleList = listOf(
            ScheduleFactory.createSchedule(scheduleId = null, repeatWorkId = repeatWorkId),
            ScheduleFactory.createSchedule(scheduleId = null, repeatWorkId = repeatWorkId)

        )
        val expected = listOf(1L, 2L)
        every { database.scheduleDao.insert(scheduleEntityList) } returns expected

        // do
        val actual = underTest.insert(scheduleList)

        // check
        verify { database.scheduleDao.insert(scheduleEntityList) }
        assertEquals(expected, actual)
    }


    @Test
    fun update() {
        // prepare
        val repeatWorkId = 1L
        val scheduleEntityList = listOf(
            ScheduleFactory.createScheduleEntity(scheduleId = null, repeatWorkId = repeatWorkId),
            ScheduleFactory.createScheduleEntity(scheduleId = null, repeatWorkId = repeatWorkId)

        )
        val scheduleList = listOf(
            ScheduleFactory.createSchedule(scheduleId = null, repeatWorkId = repeatWorkId),
            ScheduleFactory.createSchedule(scheduleId = null, repeatWorkId = repeatWorkId)

        )
        every { database.scheduleDao.update(scheduleEntityList) } returns Unit

        // do
        underTest.update(scheduleList)

        // check
        verify { database.scheduleDao.update(scheduleEntityList) }
    }

    @Test
    fun deleteAll() {
        // prepare
        val repeatWorkId = 1L
        every { database.scheduleDao.deleteAll(repeatWorkId) } returns Unit

        // do
        underTest.deleteAll(repeatWorkId)

        // check
        verify { database.scheduleDao.deleteAll(repeatWorkId) }
    }
}