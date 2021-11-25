package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.common.factory.FlowerbedFactory
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File


/**
 * Тесты для класса FlowerbedInteractorImpl
 *
 * Created by i.repkina on 15.11.2021.
 */
class FlowerbedInteractorImplTest {
    private val flowerbedRepository = mockk<FlowerbedRepository>(relaxUnitFun = true)
    private val dirRepository = mockk<DirRepository>(relaxUnitFun = true)

    private val underTest = FlowerbedInteractorImpl(flowerbedRepository, dirRepository)

    @Test
    fun getFlowerbedAll() {
        // prepare
        val expected = listOf(
            FlowerbedFactory.createFlowerbed(1),
            FlowerbedFactory.createFlowerbed(2)
        )
        val repositoryResult = listOf(
            FlowerbedFactory.createFlowerbed(2),
            FlowerbedFactory.createFlowerbed(1)
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
        val expected = FlowerbedFactory.createFlowerbed(7)
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
        val flowerbed = FlowerbedFactory.createFlowerbed(null)
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
        val flowerbed = FlowerbedFactory.createFlowerbed(null)

        // do
        underTest.updateFlowerbed(flowerbed)

        // check
        verify { flowerbedRepository.updateFlowerbed(flowerbed) }
    }

    @Test
    fun deleteFlowerbed() {
        // prepare
        val flowerbedId = 1L
        val flowerbed = FlowerbedFactory.createFlowerbed(flowerbedId)
        val file = mockk<File>(relaxed = true) {
            every { exists() } returns true
        }
        every { dirRepository.getFlowerbedDir(flowerbedId) } returns file

        // do
        underTest.deleteFlowerbed(flowerbed)

        // check
        verify { dirRepository.getFlowerbedDir(flowerbedId) }
        verify { file.deleteRecursively() }
    }

    @Test(expected = IllegalStateException::class)
    fun deleteFlowerbedEmptyId() {
        // prepare
        val flowerbed = FlowerbedFactory.createFlowerbed(null)

        // do
        underTest.deleteFlowerbed(flowerbed)
    }
}