package com.irepka3.mygarden.data.repository

import com.irepka3.mygarden.common.factory.FlowerbedPhotoFactory
import com.irepka3.mygarden.data.database.AppRoomDataBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты для класса FlowerbedPhotoRepositoryImpl
 *
 * Created by i.repkina on 22.11.2021.
 */
class FlowerbedPhotoRepositoryImplTest {
    private val database = mockk<AppRoomDataBase>(relaxUnitFun = true)
    private val underTest = FlowerbedPhotoRepositoryImpl(database)

    @Test
    fun getAllByFlowerbedId() {
        // prepare
        val flowerbedId = 1L
        val expected = listOf(
            FlowerbedPhotoFactory.createFlowerbedPhoto(flowerbedId, 1),
            FlowerbedPhotoFactory.createFlowerbedPhoto(flowerbedId, 2)
        )
        val databaseResult = listOf(
            FlowerbedPhotoFactory.createFlowerbedPhotoEntity(1, flowerbedId),
            FlowerbedPhotoFactory.createFlowerbedPhotoEntity(2, flowerbedId)
        )
        every { database.flowerbedPhotoDao.getAllByFlowerbedId(flowerbedId) } returns databaseResult

        // do
        val actual = underTest.getAllByFlowerbedId(flowerbedId)

        // check
        verify { database.flowerbedPhotoDao.getAllByFlowerbedId(flowerbedId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertFlowerbedPhoto() {
        // prepare
        val flowerbedId = 1L
        val flowerbedPhoto = FlowerbedPhotoFactory.createFlowerbedPhoto(flowerbedId, 1)
        val flowerbedPhotoEntity = FlowerbedPhotoFactory.createFlowerbedPhotoEntity(1, flowerbedId)
        every { database.flowerbedPhotoDao.insert(flowerbedPhotoEntity) } returns Unit

        // do
        underTest.insertFlowerbedPhoto(flowerbedPhoto)

        // check
        verify { database.flowerbedPhotoDao.insert(flowerbedPhotoEntity) }
    }

    @Test
    fun deleteFlowerbedPhoto() {
        // prepare
        val flowerbedId = 1L
        val flowerbedPhotoList = listOf(
            FlowerbedPhotoFactory.createFlowerbedPhoto(flowerbedId, 1),
            FlowerbedPhotoFactory.createFlowerbedPhoto(flowerbedId, 2)
        )
        val flowerbedPhotoEntityList = listOf(
            FlowerbedPhotoFactory.createFlowerbedPhotoEntity(1, flowerbedId),
            FlowerbedPhotoFactory.createFlowerbedPhotoEntity(2, flowerbedId)
        )
        every { database.flowerbedPhotoDao.delete(flowerbedPhotoEntityList) } returns Unit

        // do
        underTest.deleteFlowerbedPhoto(flowerbedPhotoList)

        // check
        verify { database.flowerbedPhotoDao.delete(flowerbedPhotoEntityList) }
    }

    @Test
    fun updateSelectedPhoto() {
        // prepare
        val flowerbedId = 1L
        val flowerbedPhotoId = 1L
        every {
            database.flowerbedPhotoDao.updateSelectedPhoto(
                flowerbedId = flowerbedId,
                flowerbedPhotoId = flowerbedPhotoId
            )
        } returns Unit

        // do
        underTest.updateSelectedPhoto(
            flowerbedId = flowerbedId,
            flowerbedPhotoId = flowerbedPhotoId
        )

        // check
        verify {
            database.flowerbedPhotoDao.updateSelectedPhoto(
                flowerbedId = flowerbedId, flowerbedPhotoId = flowerbedPhotoId
            )
        }
    }
}
