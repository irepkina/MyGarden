package com.irepka3.mygarden.domain

import com.irepka3.mygarden.domain.interactor.FlowerbedInteractorImpl
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Тесты для класса FlowerbedInteractorImpl
 *
 * Created by i.repkina on 15.11.2021.
 */
class FlowerbedInteractorImplTest {
    private val flowerbedRepository = mockk<FlowerbedRepository>(relaxed = true)
    private val dirRepository = mockk<DirRepository>(relaxed = true)

    private val underTest = FlowerbedInteractorImpl(flowerbedRepository, dirRepository)

    @Test
    fun getFlowerbedAll() {
        // prepare
        val expected = listOf(
            Flowerbed(flowerbedId = 1),
            Flowerbed(flowerbedId = 2)
        )
        val repositoryResult = listOf(
            Flowerbed(flowerbedId = 2),
            Flowerbed(flowerbedId = 1)
        )
        every { flowerbedRepository.getFlowerbedAll() } returns repositoryResult

        // do
        val actual = underTest.getFlowerbedAll()

        // check
        verify { flowerbedRepository.getFlowerbedAll() }
        assertEquals(expected, actual)
    }


    @Test
    fun getFlowerbed() {
        // prepare
        val expected = Flowerbed(flowerbedId = 7)
        every { flowerbedRepository.getFlowerbed(7) } returns expected

        // do
        val actual = underTest.getFlowerbed(7)

        // check
        verify { flowerbedRepository.getFlowerbed(7) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertFlowerbed() {
        // prepare
        val flowerbed = Flowerbed(flowerbedId = null)
        val expected = 1L
        every { flowerbedRepository.insertFlowerbed(flowerbed) } returns expected

        // do
        val actual = underTest.insertFlowerbed(flowerbed)

        // check
        verify { flowerbedRepository.insertFlowerbed(flowerbed) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateFlowerbed() {
        // prepare
        val flowerbed = Flowerbed(flowerbedId = null)

        // do
        underTest.updateFlowerbed(flowerbed)

        // check
        verify { flowerbedRepository.updateFlowerbed(flowerbed) }
    }

    @Test
    fun deleteFlowerbed() {
        // prepare
        val flowerbed = Flowerbed(flowerbedId = 1)

        // do
        underTest.deleteFlowerbed(flowerbed)

        // check
        verify { dirRepository.getFlowerbedDir(1) }
    }

    @Test(expected = IllegalStateException::class)
    fun deleteFlowerbedEmptyId() {
        // prepare
        val flowerbed = Flowerbed(flowerbedId = null)

        // do
        underTest.deleteFlowerbed(flowerbed)
    }
}