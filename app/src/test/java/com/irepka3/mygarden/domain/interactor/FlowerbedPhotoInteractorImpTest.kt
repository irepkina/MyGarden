package com.irepka3.mygarden.domain

import com.irepka3.mygarden.common.factory.FlowerbedFactory
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractorImpl
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Тесты для класса FlowerbedPhotoInteractorImpl
 *
 * Created by i.repkina on 16.11.2021.
 */
class FlowerbedPhotoInteractorImplTest {
    private val flowerbedPhotoRepository = mockk<FlowerbedPhotoRepository>(relaxUnitFun = true)
    private val dirRepository = mockk<DirRepository>(relaxUnitFun = true)

    private val underTest = FlowerbedPhotoInteractorImpl(dirRepository, flowerbedPhotoRepository)

    @Test
    fun getAllByFlowerbedId() {
        // prepare
        val flowerbedId = 1L
        val repositoryResult = listOf(
            FlowerbedFactory.createFlowerbedPhoto(1L, 2L),
            FlowerbedFactory.createFlowerbedPhoto(1L, 1L)
        )

        val expected = listOf(
            FlowerbedFactory.createFlowerbedPhoto(1L, 1L),
            FlowerbedFactory.createFlowerbedPhoto(1L, 2L),
        )

        every { flowerbedPhotoRepository.getAllByFlowerbedId(flowerbedId) } returns repositoryResult

        // do
        val actual = underTest.getAllByFlowerbedId(flowerbedId)

        // check
        verify { flowerbedPhotoRepository.getAllByFlowerbedId(flowerbedId) }
        assertEquals(expected, actual)
    }

    @Test
    fun insertFlowerbedPhoto() {
        // prepare
        val flowerbedPhoto = FlowerbedFactory.createFlowerbedPhoto(1L, null)

        // do
        underTest.insertFlowerbedPhoto(flowerbedPhoto)

        // check
        verify { flowerbedPhotoRepository.insertFlowerbedPhoto(flowerbedPhoto) }
    }

    @Test
    fun deleteFlowerbedPhoto() {
        // prepare
        val flowerbedPhoto = listOf(
            FlowerbedFactory.createFlowerbedPhoto(1L, 1L),
            FlowerbedFactory.createFlowerbedPhoto(1L, 2L)
        )

        // do
        underTest.deleteFlowerbedPhoto(flowerbedPhoto)

        // check
        verify { flowerbedPhotoRepository.deleteFlowerbedPhoto(flowerbedPhoto) }
    }

    @Test
    fun updateSelectedPhoto() {
        // prepare
        val flowerbedId = 1L
        val flowerbedPhotoId = 1L

        // do
        underTest.updateSelectedPhoto(flowerbedId, flowerbedPhotoId)

        // check
        verify { flowerbedPhotoRepository.updateSelectedPhoto(flowerbedId, flowerbedPhotoId) }
    }

    @Test
    fun getFlowerbedDir() {
        // prepare
        val flowerbedId = 1L
        val expected = mockk<File>()
        every { dirRepository.getFlowerbedDir(flowerbedId) } returns expected

        // do
        val actual = underTest.getFlowerbedDir(flowerbedId)

        // check
        verify { dirRepository.getFlowerbedDir(flowerbedId) }
        assertEquals(expected, actual)
    }

}