package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.common.factory.RepeatWorkFactory
import com.irepka3.mygarden.common.factory.ScheduleFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса RepeatWorkRepositoryImpl
 *
 * Created by i.repkina on 23.11.2021.
 */
class RepeatWorkRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = RepeatWorkRepositoryImpl(database)

    @Test
    fun getAllActive() {
        // prepare
        val repeatWorkWithScheduele = listOf(
            RepeatWorkFactory.createRepeatWorkWithScheduele(1),
            RepeatWorkFactory.createRepeatWorkWithScheduele(2)
        )
        val expected = listOf(
            RepeatWorkFactory.createRepeatWork(1),
            RepeatWorkFactory.createRepeatWork(2)
        )
        every { database.repeatWorkDao.getAllActive() } returns repeatWorkWithScheduele

        // do
        val actual = underTest.getAllActive()

        // check
        assertEquals(expected, actual)
    }

    @Test
    fun getAllActiveByMonth() {
        // prepare
        val month = 1
        val week = 1
        val repeatWorkWithScheduele = listOf(
            RepeatWorkFactory.createRepeatWorkWithSchedueleMonthDay(
                repeatWorkId = 1,
                month = month,
                week = week
            ),
            RepeatWorkFactory.createRepeatWorkWithSchedueleMonthDay(
                repeatWorkId = 2,
                month = 2,
                week = week
            ),
            RepeatWorkFactory.createRepeatWorkWithSchedueleMonthDay(
                repeatWorkId = 2,
                month = null,
                week = week
            )
        )
        val expected = listOf(
            RepeatWorkFactory.createRepeatWorkMonthDay(
                repeatWorkId = 1,
                month = month,
                week = week
            ),
            RepeatWorkFactory.createRepeatWorkMonthDay(repeatWorkId = 2, month = null, week = week)
        )
        every { database.repeatWorkDao.getAllActive() } returns repeatWorkWithScheduele

        // do
        val actual = underTest.getAllActiveByMonth(month)

        // check
        verify { database.repeatWorkDao.getAllActive() }
        assertEquals(expected, actual)
    }

    @Test
    fun getById() {
        // prepare
        val repeatWorkId = 1L
        val repeatWorkWithScheduele =
            RepeatWorkFactory.createRepeatWorkWithScheduele(repeatWorkId = 1L)
        val expected = RepeatWorkFactory.createRepeatWork(repeatWorkId = 1L)
        every { database.repeatWorkDao.getById(repeatWorkId) } returns repeatWorkWithScheduele

        // do
        val actual = underTest.getById(repeatWorkId)

        // check
        verify { database.repeatWorkDao.getById(repeatWorkId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertRepeatWork() {
        // prepare
        val scheduleIdFirst = 1L
        val scheduleIdSecond = 2L
        val expected = 1L

        val repeatWork = RepeatWorkFactory.createNewRepeat()
        val repeatWorkEntity = RepeatWorkFactory.createRepeatWorkEntity(null)
        val schedules = listOf(
            ScheduleFactory.createScheduleEntity(scheduleId = scheduleIdFirst, expected),
            ScheduleFactory.createScheduleEntity(scheduleId = scheduleIdSecond, expected)
        )

        every { database.repeatWorkDao.insert(repeatWorkEntity) } returns expected
        every { database.scheduleDao.insert(schedules) } returns listOf(
            scheduleIdFirst,
            scheduleIdSecond
        )

        // do
        val actual = underTest.insertRepeatWork(repeatWork)

        // check
        verify { database.repeatWorkDao.insert(repeatWorkEntity) }
        verify { database.scheduleDao.insert(schedules) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateRepeatWork() {
        // prepare
        val repeatWorkId = 1L
        val scheduleIdFirst = 1L
        val scheduleIdSecond = 2L

        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val repeatWorkEntity = RepeatWorkFactory.createRepeatWorkEntity(repeatWorkId)
        val schedules = listOf(
            ScheduleFactory.createScheduleEntity(scheduleId = scheduleIdFirst, repeatWorkId),
            ScheduleFactory.createScheduleEntity(scheduleId = scheduleIdSecond, repeatWorkId)
        )

        every { database.repeatWorkDao.update(repeatWorkEntity) } returns Unit
        every { database.scheduleDao.deleteAll(repeatWorkId) } returns Unit
        every { database.scheduleDao.insert(schedules) } returns listOf(
            scheduleIdFirst,
            scheduleIdSecond
        )

        // do
        underTest.updateRepeatWork(repeatWork)

        // check
        verify { database.repeatWorkDao.update(repeatWorkEntity) }
        verify { database.scheduleDao.deleteAll(repeatWorkId) }
        verify { database.scheduleDao.insert(schedules) }
    }

    @Test
    fun deleteRepeatWork() {
        // prepare
        val repeatWorkId = 1L

        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val repeatWorkEntity = RepeatWorkFactory.createRepeatWorkEntity(repeatWorkId)

        every { database.repeatWorkDao.delete(repeatWorkEntity) } returns Unit

        // do
        underTest.deleteRepeatWork(repeatWork)

        // check
        verify { database.repeatWorkDao.delete(repeatWorkEntity) }
    }
}