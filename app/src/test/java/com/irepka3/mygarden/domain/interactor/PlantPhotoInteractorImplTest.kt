package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.common.factory.PlantPhotoFactory
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.PlantPhotoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Тесты для класса PlantPhotoInteractorImpl
 *
 * Created by i.repkina on 21.11.2021.
 */
class PlantPhotoInteractorImplTest {
    private val dirRepository = mockk<DirRepository>(relaxUnitFun = true)
    private val plantPhotoRepository = mockk<PlantPhotoRepository>(relaxUnitFun = true)
    private val underTest = PlantPhotoInteractorImpl(dirRepository, plantPhotoRepository)

    @Test
    fun getAllByPlantId() {
        // prepare
        val plantId = 1L
        val expected = listOf(
            PlantPhotoFactory.createPlantPhoto(plantId, 1),
            PlantPhotoFactory.createPlantPhoto(plantId, 2)
        )
        val repositoryResult = listOf(
            PlantPhotoFactory.createPlantPhoto(plantId, 2),
            PlantPhotoFactory.createPlantPhoto(plantId, 1)
        )
        every { plantPhotoRepository.getAllByPlantId(plantId) } returns repositoryResult

        // do
        val actual = underTest.getAllByPlantId(plantId)

        // check
        verify { plantPhotoRepository.getAllByPlantId(plantId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertPlantPhoto() {
        // prepare
        val plantPhoto = PlantPhotoFactory.createPlantPhoto(1, 1)
        every { plantPhotoRepository.insertPlantPhoto(plantPhoto) } returns Unit

        // do
        underTest.insertPlantPhoto(plantPhoto)

        // check
        verify { plantPhotoRepository.insertPlantPhoto(plantPhoto) }
    }

    @Test
    fun deletePlantPhoto() {
        // prepare
        val plantId = 1L
        val plantPhotoList = listOf(
            PlantPhotoFactory.createPlantPhoto(plantId, 1),
            PlantPhotoFactory.createPlantPhoto(plantId, 2)
        )
        every { plantPhotoRepository.deletePlantPhoto(plantPhotoList) } returns Unit

        // do
        underTest.deletePlantPhoto(plantPhotoList)

        // check
        verify { plantPhotoRepository.deletePlantPhoto(plantPhotoList) }
    }

    @Test
    fun getPlantDir() {
        // prepare
        val plantId = 1L
        val flowerbedId = 1L
        val expected = File("uri")
        every {
            dirRepository.getPlantDir(
                flowerbedId = flowerbedId,
                plantId = plantId
            )
        } returns expected

        // do
        val actual = underTest.getPlantDir(flowerbedId = flowerbedId, plantId = plantId)

        // check
        verify { dirRepository.getPlantDir(flowerbedId = flowerbedId, plantId = plantId) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateSelectedPhoto() {
        // prepare
        val plantId = 1L
        val plantPhotoId = 1L
        every {
            plantPhotoRepository.updateSelectedPhoto(
                plantId = plantId,
                plantPhotoId = plantPhotoId
            )
        } returns Unit

        // do
        underTest.updateSelectedPhoto(plantId = plantId, plantPhotoId = plantPhotoId)

        // check
        verify {
            plantPhotoRepository.updateSelectedPhoto(
                plantId = plantId,
                plantPhotoId = plantPhotoId
            )
        }
    }
}