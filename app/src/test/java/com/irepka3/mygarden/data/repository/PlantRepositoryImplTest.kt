package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.common.factory.PlantFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса PlantRepositoryImpl
 *
 * Created by i.repkina on 22.11.2021.
 */
class PlantRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = PlantRepositoryImpl(database)

    @Test
    fun getPlantsByFlowerbed() {
        // prepare
        val flowerbedID = 1L
        val expected = listOf(
            PlantFactory.createPlant(flowerbedID, 1),
            PlantFactory.createPlant(flowerbedID, 2)
        )
        val databaseResult = listOf(
            PlantFactory.createPlantWithPhoto(flowerbedID, 1),
            PlantFactory.createPlantWithPhoto(flowerbedID, 2)
        )
        every { database.plantDao.getPlantsByFlowerBedId(flowerbedID) } returns databaseResult

        // do
        val actual = underTest.getPlantsByFlowerbed(flowerbedID)

        // check
        verify { database.plantDao.getPlantsByFlowerBedId(flowerbedID) }
        assertEquals(expected, actual)
    }

    @Test
    fun getPlant() {
        // prepare
        val flowerbedId = 1L
        val plantId = 1L
        val plantEntity =
            PlantFactory.createPlantEntity(flowerbedId = flowerbedId, plantId = plantId)
        val expected =
            PlantFactory.createPlantEmptyUri(flowerbedId = flowerbedId, plantId = plantId)
        every { database.plantDao.getById(plantId) } returns plantEntity

        // do
        val actual = underTest.getPlant(plantId)

        // check
        verify { database.plantDao.getById(plantId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertPlant() {
        // prepare
        val flowerbedId = 1L
        val expected = 1L
        val plant = PlantFactory.createPlant(flowerbedId = flowerbedId, null)
        val plantEntity = PlantFactory.createPlantEntity(flowerbedId = flowerbedId, null)
        every { database.plantDao.insert(plantEntity) } returns expected

        // do
        val actual = underTest.insertPlant(plant)

        // check
        verify { database.plantDao.insert(plantEntity) }
        assertEquals(expected, actual)
    }

    @Test
    fun updatePlant() {
        // prepare
        val flowerbedId = 1L
        val plantId = 1L
        val plant = PlantFactory.createPlant(flowerbedId = flowerbedId, plantId)
        val plantEntity = PlantFactory.createPlantEntity(flowerbedId = flowerbedId, plantId)
        every { database.plantDao.update(plantEntity) } returns Unit

        // do
        underTest.updatePlant(plant)

        // check
        verify { database.plantDao.update(plantEntity) }
    }

    @Test
    fun deletePlant() {
        // prepare
        val flowerbedId = 1L
        val plantId = 1L
        val plant = PlantFactory.createPlant(flowerbedId = flowerbedId, plantId)
        val plantEntity = PlantFactory.createPlantEntity(flowerbedId = flowerbedId, plantId)
        every { database.plantDao.delete(plantEntity) } returns Unit

        // do
        underTest.deletePlant(plant)

        // check
        verify { database.plantDao.delete(plantEntity) }
    }
}
