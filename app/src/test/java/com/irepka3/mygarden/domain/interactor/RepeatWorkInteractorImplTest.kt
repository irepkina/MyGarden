package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.common.factory.RepeatWorkFactory
import com.irepka3.mygarden.domain.repository.RepeatWorkRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса RepeatWorkInteractorImpl
 *
 * Created by i.repkina on 21.11.2021.
 */
class RepeatWorkInteractorImplTest {
    private val repeatWorkRepository = mockk<RepeatWorkRepository>(relaxUnitFun = true)

    private val underTest = RepeatWorkInteractorImpl(repeatWorkRepository)

    @Test
    fun getAllActive() {
        // prepare
        val expected =
            listOf(RepeatWorkFactory.createRepeatWork(1), RepeatWorkFactory.createRepeatWork(2))
        every { repeatWorkRepository.getAllActive() } returns expected

        // do
        val actual = underTest.getAllActive()

        // check
        verify { repeatWorkRepository.getAllActive() }
        assertEquals(expected, actual)
    }

    @Test
    fun getAllActiveByMonth() {
        // prepare
        val expected =
            listOf(RepeatWorkFactory.createRepeatWork(1), RepeatWorkFactory.createRepeatWork(2))
        val month = 1
        every { repeatWorkRepository.getAllActiveByMonth(month) } returns expected

        // do
        val actual = underTest.getAllActiveByMonth(month)

        // check
        verify { repeatWorkRepository.getAllActiveByMonth(month) }
        assertEquals(expected, actual)
    }

    @Test
    fun getById() {
        // prepare
        val repeatWorkId = 1L
        val expected = RepeatWorkFactory.createRepeatWork(repeatWorkId)
        every { repeatWorkRepository.getById(repeatWorkId) } returns expected

        // do
        val actual = underTest.getById(repeatWorkId)

        // check
        verify { repeatWorkRepository.getById(repeatWorkId) }
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalStateException::class)
    fun getByIdEmptyId() {
        // prepare
        val repeatWorkId = 1L
        every { repeatWorkRepository.getById(repeatWorkId) } returns null

        // do
        underTest.getById(repeatWorkId)

        // check
        verify { repeatWorkRepository.getById(repeatWorkId) }
    }

    @Test
    fun insertRepeatWork() {
        // prepare
        val expected = 1L
        val repeatWork = RepeatWorkFactory.createRepeatWork(expected)
        every { repeatWorkRepository.insertRepeatWork(repeatWork) } returns expected

        // do
        val actual = underTest.insertRepeatWork(repeatWork)

        // check
        verify { repeatWorkRepository.insertRepeatWork(repeatWork) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateRepeatWork() {
        // prepare
        val repeatWork = RepeatWorkFactory.createRepeatWork(1L)
        every { repeatWorkRepository.updateRepeatWork(repeatWork) } returns Unit

        // do
        underTest.updateRepeatWork(repeatWork)

        // check
        verify { repeatWorkRepository.updateRepeatWork(repeatWork) }
    }

    @Test
    fun deleteRepeatWork() {
        // prepare
        val repeatWork = RepeatWorkFactory.createRepeatWork(1L)
        every { repeatWorkRepository.deleteRepeatWork(repeatWork) } returns Unit

        // do
        underTest.deleteRepeatWork(repeatWork)

        // check
        verify { repeatWorkRepository.deleteRepeatWork(repeatWork) }
    }

    @Test(expected = IllegalStateException::class)
    fun deleteRepeatWorkEmptyId() {
        // prepare
        val repeatWork = RepeatWorkFactory.createRepeatWork(null)

        // do
        underTest.deleteRepeatWork(repeatWork)

        // check
        verify { repeatWorkRepository.deleteRepeatWork(repeatWork) }
    }
}