package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.common.factory.FlowerbedFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса FlowerbedRepositoryImpl
 *
 * Created by i.repkina on 15.11.2021.
 */
class FlowerbedRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = FlowerbedRepositoryImpl(database)

    @Test
    fun getFlowerbedAll() {
        // prepare
        val flowerbedWithPhotoFirst = FlowerbedFactory.createFlowerbedWithPhoto(1)
        val flowerbedWithPhotoSecond = FlowerbedFactory.createFlowerbedWithPhoto(2)
        val flowerbedWithPhotoList = listOf(flowerbedWithPhotoFirst, flowerbedWithPhotoSecond)

        val flowerbedFirst = FlowerbedFactory.createFlowerbed(1)
        val flowerbedSecond = FlowerbedFactory.createFlowerbed(2)

        val expected = listOf(flowerbedFirst, flowerbedSecond)
        every { database.flowerbedDao.getAll() } returns flowerbedWithPhotoList

        // do
        val actual = underTest.getFlowerbedAll()

        // check
        verify { database.flowerbedDao.getAll() }
        assertEquals(expected, actual)
    }

    @Test
    fun getFlowerbed() {
        // prepare
        val flowerbedId = 1L
        val flowerbedEntity = FlowerbedFactory.createFlowerbedEntity(flowerbedId)
        val expected = FlowerbedFactory.createFlowerbedEmptyUri(flowerbedId)
        every { database.flowerbedDao.getById(flowerbedId) } returns flowerbedEntity

        // do
        val actual = underTest.getFlowerbed(flowerbedId)

        // check
        verify { database.flowerbedDao.getById(flowerbedId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertFlowerbed() {
        // prepare
        val expected = 1L
        val flowerbed = FlowerbedFactory.createFlowerbed(1L)
        val flowerbedEntity = FlowerbedFactory.createFlowerbedEntity(1L)
        every { database.flowerbedDao.insert(flowerbedEntity) } returns expected

        // do
        val actual = underTest.insertFlowerbed(flowerbed)

        // check
        verify { database.flowerbedDao.insert(flowerbedEntity) }
        assertEquals(expected, actual)
    }

    @Test
    fun updateFlowerbed() {
        // prepare
        val flowerbed = FlowerbedFactory.createFlowerbed(1L)
        val flowerbedEntity = FlowerbedFactory.createFlowerbedEntity(1L)
        every { database.flowerbedDao.update(flowerbedEntity) } returns Unit

        // do
        underTest.updateFlowerbed(flowerbed)

        // check
        verify { database.flowerbedDao.update(flowerbedEntity) }
    }

    @Test
    fun deleteFlowerbed() {
        // prepare
        val flowerbed = FlowerbedFactory.createFlowerbed(1L)
        val flowerbedEntity = FlowerbedFactory.createFlowerbedEntity(1L)
        every { database.flowerbedDao.delete(flowerbedEntity) } returns Unit

        // do
        underTest.deleteFlowerbed(flowerbed)

        // check
        verify { database.flowerbedDao.delete(flowerbedEntity) }
    }
}