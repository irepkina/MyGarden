package com.irepka3.mygarden.data.repository


import com.irepka3.mygarden.common.factory.PlantPhotoFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса PlantPhotoRepositoryImpl
 * Created by i.repkina on 22.11.2021.
 */
class PlantPhotoRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = PlantPhotoRepositoryImpl(database)

    @Test
    fun getAllByPlantId() {
        // prepare
        val plantId = 1L
        val expected = listOf(
            PlantPhotoFactory.createPlantPhoto(plantId, 1),
            PlantPhotoFactory.createPlantPhoto(plantId, 2)
        )
        val databaseResult = listOf(
            PlantPhotoFactory.createPlantPhotoEntity(plantId, 1),
            PlantPhotoFactory.createPlantPhotoEntity(plantId, 2)
        )
        every { database.plantPhotoDao.getAllByPlantId(plantId) } returns databaseResult

        // do
        val actual = underTest.getAllByPlantId(plantId)

        // check
        verify { database.plantPhotoDao.getAllByPlantId(plantId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertPlantPhoto() {
        // prepare
        val plantId = 1L
        val plantPhoto = PlantPhotoFactory.createPlantPhoto(plantId, 1)
        val plantPhotoEntity = PlantPhotoFactory.createPlantPhotoEntity(plantId, 1)
        every { database.plantPhotoDao.insert(plantPhotoEntity) } returns Unit

        // do
        underTest.insertPlantPhoto(plantPhoto)

        // check
        verify { database.plantPhotoDao.insert(plantPhotoEntity) }
    }

    @Test
    fun deletePlantPhoto() {
        // prepare
        val plantId = 1L
        val plantPhotoList = listOf(
            PlantPhotoFactory.createPlantPhoto(plantId, 1),
            PlantPhotoFactory.createPlantPhoto(plantId, 2)
        )
        val plantPhotoEntityList = listOf(
            PlantPhotoFactory.createPlantPhotoEntity(plantId, 1),
            PlantPhotoFactory.createPlantPhotoEntity(plantId, 2)
        )
        every { database.plantPhotoDao.delete(plantPhotoEntityList) } returns Unit

        // do
        underTest.deletePlantPhoto(plantPhotoList)

        // check
        verify { database.plantPhotoDao.delete(plantPhotoEntityList) }
    }

    @Test
    fun updateSelectedPhoto() {
        // prepare
        val plantId = 1L
        val plantPhotoId = 1L

        every {
            database.plantPhotoDao.updateSelectedPhoto(
                plantId = plantId,
                plantPhotoId = plantPhotoId
            )
        } returns Unit

        // do
        underTest.updateSelectedPhoto(plantId = plantId, plantPhotoId = plantPhotoId)

        // check
        verify {
            database.plantPhotoDao.updateSelectedPhoto(
                plantId = plantId,
                plantPhotoId = plantPhotoId
            )
        }
    }

}
