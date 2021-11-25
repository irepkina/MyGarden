package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.common.factory.PlantFactory
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.PlantRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Тесты для класса PlantInteractorImpl
 *
 * Created by i.repkina on 16.11.2021.
 */
class PlantInteractorImplTest {
    private val plantRepository = mockk<PlantRepository>(relaxUnitFun = true)
    private val dirRepository = mockk<DirRepository>()

    private val underTest = PlantInteractorImpl(plantRepository, dirRepository)

    @Test
    fun getPlantsByFlowerbed() {
        // prepare
        val flowerbedId = 1L
        val plantList = listOf(
            PlantFactory.createPlant(1L, 2L),
            PlantFactory.createPlant(1L, 1L)
        )
        val expected = listOf(
            PlantFactory.createPlant(1L, 1L),
            PlantFactory.createPlant(1L, 2L)
        )
        every { plantRepository.getPlantsByFlowerbed(flowerbedId) } returns plantList

        // do
        val actual = underTest.getPlantsByFlowerbed(flowerbedId)

        // check
        verify { plantRepository.getPlantsByFlowerbed(flowerbedId) }
        assertEquals(expected, actual)
    }

    @Test
    fun getPlant() {
        // prepare
        val flowerbedId = 1L
        val plantId = 1L
        val expected = PlantFactory.createPlant(flowerbedId, plantId)
        every { plantRepository.getPlant(plantId) } returns expected

        // do
        val actual = underTest.getPlant(plantId)

        // check
        verify { plantRepository.getPlant(plantId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertPlant() {
        // prepare
        val flowerbedId = 1L
        val expected = 1L
        val plant = PlantFactory.createPlant(flowerbedId, null)
        every { plantRepository.insertPlant(plant) } returns expected

        // do
        val actual = underTest.insertPlant(plant)

        // check
        verify { plantRepository.insertPlant(plant) }
        assertEquals(expected, actual)
    }

    @Test
    fun updatePlant() {
        // prepare
        val flowerbedId = 1L
        val plantId = 1L
        val plant = PlantFactory.createPlant(flowerbedId, plantId)

        // do
        underTest.updatePlant(plant)

        // check
        verify { plantRepository.updatePlant(plant) }
    }

    @Test
    fun deletePlant() {
        // prepare
        val flowerbedId = 1L
        val plantId = 1L
        val plant = PlantFactory.createPlant(flowerbedId, plantId)
        val file = mockk<File>(relaxed = true) {
            every { exists() } returns true
        }
        every {
            dirRepository.getPlantDir(
                flowerbedId = flowerbedId,
                plantId = plantId
            )
        } returns file

        // do
        underTest.deletePlant(plant)

        // check
        verify { plantRepository.deletePlant(plant) }
        verify { dirRepository.getPlantDir(flowerbedId = flowerbedId, plantId = plantId) }
        verify { file.deleteRecursively() }
    }

    @Test(expected = IllegalStateException::class)
    fun deletePlantError() {
        // prepare
        val flowerbedId = 1L
        val plantId = null
        val plant = PlantFactory.createPlant(flowerbedId, plantId)

        // do
        underTest.deletePlant(plant)
    }
}