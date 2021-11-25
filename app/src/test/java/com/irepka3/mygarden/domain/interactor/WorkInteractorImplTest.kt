package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.common.factory.WorkFactory
import com.irepka3.mygarden.domain.repository.WorkRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса WorkInteractorImpl
 *
 * Created by i.repkina on 21.11.2021.
 */
class WorkInteractorImplTest {
    private val workRepository = mockk<WorkRepository>(relaxUnitFun = true)

    private val underTest = WorkInteractorImpl(workRepository)

    @Test
    fun getAll() {
        // prepare
        val dateFrom = 1L
        val dateTo = 1L
        val expected = listOf(
            WorkFactory.createWork(1, null, null),
            WorkFactory.createWork(2, null, null)
        )
        every { workRepository.getAll(dateFrom, dateTo) } returns expected

        // do
        val actual = underTest.getAll(dateFrom, dateTo)

        // check
        verify { workRepository.getAll(dateFrom, dateTo) }
        assertEquals(expected, actual)
    }

    @Test
    fun getById() {
        // prepare
        val workId = 1L
        val expected = WorkFactory.createWork(workId, null, null)
        every { workRepository.getById(workId) } returns expected

        // do
        val actual = underTest.getById(workId)

        // check
        verify { workRepository.getById(workId) }
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalStateException::class)
    fun getByIdWorkNotFound() {
        // prepare
        val workId = 1L
        every { workRepository.getById(workId) } returns null

        // do
        underTest.getById(workId)
    }

    @Test
    fun insertWork() {
        // prepare
        val work = WorkFactory.createWork(1L, null, null)
        val expected = 1L
        every { workRepository.insertWork(work) } returns expected

        // do
        val actual = underTest.insertWork(work)

        // check
        verify { workRepository.insertWork(work) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateWork() {
        // prepare
        val work = WorkFactory.createWork(1L, null, null)
        every { workRepository.updateWork(work) } returns Unit

        // do
        underTest.updateWork(work)

        // check
        verify { workRepository.updateWork(work) }
    }

    @Test
    fun deleteWork() {
        // prepare
        val work = WorkFactory.createWork(1L, null, null)
        every { workRepository.deleteWork(work) } returns Unit

        // do
        underTest.deleteWork(work)

        // check
        verify { workRepository.deleteWork(work) }
    }

    @Test(expected = IllegalStateException::class)
    fun deleteWorkEmptyId() {
        // prepare
        val work = WorkFactory.createWork(null, null, null)

        // do
        underTest.deleteWork(work)
    }

}