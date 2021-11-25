package com.irepka3.mygarden.ui.work.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.WorkFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractor
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.util.date.toMillis
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

/**
 * Тесты для вью-модели WorkListViewModel
 *
 * Created by i.repkina on 23.11.2021.
 */
class WorkListViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val interactor = mockk<WorkManagerInteractor>(relaxed = true)
    private val underTest = WorkListViewModel(interactor)

    @Test
    fun onCreateView() {
        // prepare
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month.value
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val workList = listOf(
            WorkFactory.createWork(1L, currentDate.toMillis(), null),
            WorkFactory.createWork(2L, currentDate.toMillis(), null)
        )
        every { interactor.getAll(year, month) } returns workList

        // do
        underTest.onCreateView()

        // check
        verify { interactor.getAll(year, month) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(workList, underTest.workLiveData.value)
        assertEquals(firstDayOfMonth, underTest.periodLiveData.value)
    }

    @Test
    fun onDeleteOnceWork() {
        // prepare
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month.value
        val workList = listOf(
            WorkFactory.createWork(1L, currentDate.toMillis(), null),
            WorkFactory.createWork(2L, currentDate.toMillis(), null)
        )
        val work = WorkFactory.createWork(1L, currentDate.toMillis(), null)
        val isOnceWork = true
        every { interactor.getAll(year, month) } returns workList
        every { interactor.delete(isOnceWork, work) } returns Unit

        // do
        underTest.onDelete(work)

        // check
        verify { interactor.delete(isOnceWork, work) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(workList, underTest.workLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(null, underTest.periodLiveData.value)
    }

    @Test
    fun onDeleteRepeatWork() {
        // prepare
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month.value
        val workList = listOf(
            WorkFactory.createWork(null, currentDate.toMillis(), RepeatWork(repeatWorkId = 1L)),
            WorkFactory.createWork(null, currentDate.toMillis(), RepeatWork(repeatWorkId = 2L))
        )
        val work =
            WorkFactory.createWork(null, currentDate.toMillis(), RepeatWork(repeatWorkId = 1L))
        val isOnceWork = false
        every { interactor.getAll(year, month) } returns workList
        every { interactor.delete(isOnceWork, work) } returns Unit

        // do
        underTest.onDelete(work)

        // check
        verify { interactor.delete(isOnceWork, work) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(workList, underTest.workLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(null, underTest.periodLiveData.value)
    }

    @Test
    fun onDeleteError() {
        // prepare
        val exception = IllegalStateException("Invalid workId, workId is null")
        val currentDate = LocalDate.now()
        val work = WorkFactory.createWork(null, currentDate.toMillis(), null)

        // do
        underTest.onDelete(work)

        // check
        assertEquals(exception.message, underTest.errorsLiveData.value?.message)
    }

    @Test
    fun onNextClick() {
        // prepare
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month.value
        val nextMonth = LocalDate.of(year, month + 1, 1)
        val workList = listOf(
            WorkFactory.createWork(1L, currentDate.toMillis(), null),
            WorkFactory.createWork(2L, currentDate.toMillis(), null)
        )
        every { interactor.getAll(year, month) } returns workList
        underTest.onCreateView()

        // do
        underTest.onNextClick()

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(workList, underTest.workLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(nextMonth, underTest.periodLiveData.value)
    }

    @Test
    fun onPreviousClick() {
        // prepare
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month.value
        val nextMonth = LocalDate.of(year, month - 1, 1)
        val workList = listOf(
            WorkFactory.createWork(1L, currentDate.toMillis(), null),
            WorkFactory.createWork(2L, currentDate.toMillis(), null)
        )
        every { interactor.getAll(year, month) } returns workList
        underTest.onCreateView()

        // do
        underTest.onPreviousClick()

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(workList, underTest.workLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(nextMonth, underTest.periodLiveData.value)
    }

}