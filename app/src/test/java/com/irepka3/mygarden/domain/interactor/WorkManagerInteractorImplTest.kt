package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.common.factory.RepeatWorkFactory
import com.irepka3.mygarden.common.factory.ScheduleFactory
import com.irepka3.mygarden.common.factory.WorkFactory
import com.irepka3.mygarden.domain.util.date.toMillis
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * Тесты для класса WorkManagerInteractorImpl
 *
 * Created by i.repkina on 21.11.2021.
 */
class WorkManagerInteractorImplTest {
    private val workInteractor = mockk<WorkInteractor>(relaxUnitFun = true)
    private val repeatWorkInteractor = mockk<RepeatWorkInteractor>(relaxUnitFun = true)
    private val underTest = WorkManagerInteractorImpl(workInteractor, repeatWorkInteractor)

    @Test
    fun getAll() {
        // prepare
        val year = 2021
        val month = 11
        val dateFrom = LocalDate.of(year, month, 1)
        val dateTo = dateFrom.withDayOfMonth(dateFrom.lengthOfMonth())

        val repeatWork = RepeatWorkFactory.createRepeatWork(2)
        repeatWork.schedules[0].month = month
        repeatWork.schedules[0].week = 1
        repeatWork.schedules[0].monday = true
        repeatWork.schedules[0].tuesday = true
        repeatWork.schedules[1].month = month
        repeatWork.schedules[1].week = 2
        repeatWork.schedules[1].monday = true

        val workList = listOf(WorkFactory.createWork(1, dateFrom.toMillis(), repeatWork))
        val repeatworkList = listOf(repeatWork)


        val expected = listOf(
            WorkFactory.createWork(1, dateFrom.toMillis(), repeatWork),
            WorkFactory.createWork(null, LocalDate.of(year, month, 2).toMillis(), repeatWork),
            WorkFactory.createWork(null, LocalDate.of(year, month, 8).toMillis(), repeatWork)
        )
        every { workInteractor.getAll(dateFrom.toMillis(), dateTo.toMillis()) } returns workList
        every { repeatWorkInteractor.getAllActiveByMonth(month) } returns repeatworkList

        // do
        val actual = underTest.getAll(year, month)

        // check
        assertEquals(expected, actual)
    }

    @Test
    fun insertWork() {
        // prepare
        val isOnceWork = true
        val repeatWork = RepeatWorkFactory.createRepeatWork(1L)
        val work = WorkFactory.createWork(1, null, null)
        val expected = 1L
        every { workInteractor.insertWork(work) } returns expected

        // do
        val actual = underTest.insert(isOnceWork, work)

        // check
        verify { workInteractor.insertWork(work) }
        verify(exactly = 0) { repeatWorkInteractor.insertRepeatWork(repeatWork) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertRepeatWork() {
        // prepare
        val isOnceWork = false
        val repeatWork = RepeatWorkFactory.createRepeatWork(1L)
        val work = WorkFactory.createWork(1, null, repeatWork)
        val expected = 1L

        every { repeatWorkInteractor.insertRepeatWork(repeatWork) } returns expected

        // do
        val actual = underTest.insert(isOnceWork, work)

        // check
        verify(exactly = 0) { workInteractor.insertWork(work) }
        verify { repeatWorkInteractor.insertRepeatWork(repeatWork) }
        assertEquals(expected, actual)
    }

    @Test
    fun deleteWork() {
        // prepare
        val isOnceWork = true
        val work = WorkFactory.createWork(1, null, null)
        val repeatWork = RepeatWorkFactory.createRepeatWork(1L)
        val expected = Unit
        every { workInteractor.deleteWork(work) } returns expected

        // do
        val actual = underTest.delete(isOnceWork, work)

        // check
        verify { workInteractor.deleteWork(work) }
        verify(exactly = 0) { repeatWorkInteractor.deleteRepeatWork(repeatWork) }
        assertEquals(expected, actual)
    }

    @Test
    fun deleteRepeatWork() {
        // prepare
        val isOnceWork = false
        val repeatWork = RepeatWorkFactory.createRepeatWork(1L)
        val work = WorkFactory.createWork(1, null, repeatWork)
        val expected = Unit
        every { repeatWorkInteractor.deleteRepeatWork(repeatWork) } returns expected
        // do
        val actual = underTest.delete(isOnceWork, work)

        // check
        verify(exactly = 0) { workInteractor.deleteWork(work) }
        verify { repeatWorkInteractor.deleteRepeatWork(repeatWork) }
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalStateException::class)
    fun deleteRepeatWorkError() {
        // prepare
        val isOnceWork = false
        val repeatWork = null
        val work = WorkFactory.createWork(1, null, repeatWork)

        // do
        underTest.delete(isOnceWork, work)
    }

    @Test
    fun getByIdOnceWork() {
        // prepare
        val workId = 1L
        val repeatWorkId = null
        val repeatWorkIdCheck = 1L
        val expected = WorkFactory.createWork(workId, null, null)

        every { workInteractor.getById(workId) } returns expected
        // do
        val actual = underTest.getById(workId, repeatWorkId)

        // check
        verify(exactly = 0) { repeatWorkInteractor.getById(repeatWorkIdCheck) }
        verify(exactly = 1) { workInteractor.getById(workId) }
        assertEquals(expected, actual)
    }

    @Test
    fun getByIdSavedRepeatWork() {
        // prepare
        val workId = 1L
        val repeatWorkId = 2L
        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val work = WorkFactory.createWork(workId, null, null)
        val expected = WorkFactory.createWork(workId, null, repeatWork)
        every { repeatWorkInteractor.getById(repeatWorkId) } returns repeatWork
        every { workInteractor.getById(workId) } returns work

        // do
        val actual = underTest.getById(workId, repeatWorkId)

        // check
        verify(exactly = 1) { repeatWorkInteractor.getById(repeatWorkId) }
        verify(exactly = 1) { workInteractor.getById(workId) }
        assertEquals(expected, actual)
    }

    @Test
    fun getByIdOriginalRepeatWork() {
        // prepare
        val workId = 1L
        val repeatWorkId = 1L
        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val expected = WorkFactory.createWork(repeatWorkId, null, repeatWork)
        every { repeatWorkInteractor.getById(repeatWorkId) } returns repeatWork

        // do
        val actual = underTest.getById(workId, repeatWorkId)

        // verify
        verify(exactly = 1) { repeatWorkInteractor.getById(repeatWorkId) }
        verify(exactly = 0) { workInteractor.getById(workId) }
        assertEquals(expected, actual)
    }

    @Test
    fun getByIdGenerateRepeatWork() {
        // prepare
        val workId = null
        val workIdTest = 1L
        val repeatWorkId = 1L
        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val expected = WorkFactory.createWork(null, null, repeatWork = repeatWork)
        every { repeatWorkInteractor.getById(repeatWorkId) } returns repeatWork

        // do
        val actual = underTest.getById(workId, repeatWorkId)

        // check
        verify(exactly = 1) { repeatWorkInteractor.getById(repeatWorkId) }
        verify(exactly = 0) { workInteractor.getById(workIdTest) }
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalStateException::class)
    fun getByIdGenerateRepeatWorkError() {
        // prepare
        val workId = null
        val repeatWorkId = null
        // do
        underTest.getById(workId, repeatWorkId)
    }

    @Test
    fun updateOriginalRepeatWork() {
        // prepare
        val repeatWorkId = 1L
        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val work = WorkFactory.createWork(repeatWorkId, null, repeatWork)
        every { repeatWorkInteractor.updateRepeatWork(repeatWork) } returns Unit

        // do
        underTest.update(work)

        // check
        verify(exactly = 1) { repeatWorkInteractor.updateRepeatWork(repeatWork) }
        verify(exactly = 0) { workInteractor.updateWork(work) }
        verify(exactly = 0) { workInteractor.insertWork(work) }
    }

    @Test
    fun updateOnceWork() {
        // prepare
        val repeatWorkId = null
        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val work = WorkFactory.createWork(1, null, null)

        every { workInteractor.updateWork(work) } returns Unit

        // do
        underTest.update(work)

        // check
        verify(exactly = 0) { repeatWorkInteractor.updateRepeatWork(repeatWork) }
        verify(exactly = 0) { workInteractor.insertWork(work) }
        verify(exactly = 1) { workInteractor.updateWork(work) }
    }

    @Test
    fun updateSavedRepeatWork() {
        // prepare
        val workInteractorResult = 1L
        val repeatWorkId = 2L
        val repeatWork = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        val work = WorkFactory.createWork(null, null, repeatWork)
        every { workInteractor.insertWork(work) } returns workInteractorResult

        // do
        underTest.update(work)

        // check
        verify(exactly = 1) { workInteractor.insertWork(work) }
        verify(exactly = 0) { repeatWorkInteractor.updateRepeatWork(repeatWork) }
        verify(exactly = 0) { workInteractor.updateWork(work) }
    }

    @Test(expected = IllegalStateException::class)
    fun updateError() {
        // prepare
        val repeatWork = RepeatWorkFactory.createRepeatWork(null)
        val work = WorkFactory.createWork(null, null, repeatWork)

        // do
        underTest.update(work)
    }

    @Test
    fun generateDataByWeek() {
        // prepare
        val year = 2021
        val month = 12
        val week = 1
        LocalDate.of(year, month, 1)
        val expected = listOf(
            LocalDate.of(year, month, 6).toMillis(),
            LocalDate.of(year, month, 1).toMillis(),
            LocalDate.of(year, month, 4).toMillis()
        )

        val schedule = ScheduleFactory.createScheduleWithMonthDay(
            scheduleId = 1L,
            repeatWorkId = 1L,
            month = month,
            week = week,
            monday = true,
            tuesday = false,
            wednesday = true,
            thursday = false,
            friday = false,
            saturday = true,
            sunday = false
        )

        // do
        val actual = underTest.generateDataByWeek(year, month, week, schedule)

        // check
        assertEquals(expected, actual)
    }

    @Test
    fun generateDataByWeekFebuary() {
        // prepare
        val year = 2022
        val month = 2
        val week = 4
        LocalDate.of(year, month, 1)

        val expected = listOf(
            LocalDate.of(year, month, 28).toMillis()
        )

        val schedule = ScheduleFactory.createScheduleWithMonthDay(
            scheduleId = 1L,
            repeatWorkId = 1L,
            month = month,
            week = week,
            monday = true,
            tuesday = false,
            wednesday = false,
            thursday = false,
            friday = false,
            saturday = false,
            sunday = false
        )

        // do
        val actual = underTest.generateDataByWeek(year, month, week, schedule)

        // check
        assertEquals(expected, actual)
    }

    @Test
    fun generateDataByWeekJanuary() {
        // prepare
        val year = 2022
        val month = 1
        val week = 4
        LocalDate.of(year, month, 1)

        val expected = listOf(
            LocalDate.of(year, month, 22).toMillis()
        )

        val schedule = ScheduleFactory.createScheduleWithMonthDay(
            scheduleId = 1L,
            repeatWorkId = 1L,
            month = month,
            week = week,
            monday = false,
            tuesday = false,
            wednesday = false,
            thursday = false,
            friday = false,
            saturday = true,
            sunday = false
        )

        // do
        val actual = underTest.generateDataByWeek(year, month, week, schedule)

        // check
        assertEquals(expected, actual)
    }
}
