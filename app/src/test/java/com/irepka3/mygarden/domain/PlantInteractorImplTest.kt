package com.irepka3.mygarden.domain

import com.irepka3.mygarden.domain.interactor.PlantInteractorImpl
import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.PlantRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса PlantInteractorImpl
 *
 * Created by i.repkina on 16.11.2021.
 */
class PlantInteractorImplTest {
    private val plantRepository = mockk<PlantRepository>()
    private val dirRepository = mockk<DirRepository>()

    private val underTest = PlantInteractorImpl(plantRepository, dirRepository)

    @Test
    fun getPlantsByFlowerbed() {
        // prepare
        val flowerbedId = 1L
        val plantList = listOf(
            Plant(flowerbedId = flowerbedId, plantId = 2),
            Plant(flowerbedId = flowerbedId, plantId = 1)
        )
        val expected = listOf(
            Plant(flowerbedId = flowerbedId, plantId = 1),
            Plant(flowerbedId = flowerbedId, plantId = 2)
        )

        every { plantRepository.getPlantsByFlowerbed(flowerbedId) } returns plantList

        // do
        val actual = underTest.getPlantsByFlowerbed(flowerbedId)

        // check
        assertEquals(expected, actual)
    }
}