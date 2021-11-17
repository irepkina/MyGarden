package com.irepka3.mygarden.domain

import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractorImpl
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

/**
 * Тесты для класса FlowerbedPhotoInteractorImpl
 *
 * Created by i.repkina on 16.11.2021.
 */
class FlowerbedPhotoInteractorImplTest {
    private val flowerbedPhotoRepository = mockk<FlowerbedPhotoRepository>(relaxed = true)
    private val dirRepository = mockk<DirRepository>(relaxed = true)

    private val underTest = FlowerbedPhotoInteractorImpl(dirRepository, flowerbedPhotoRepository)

    @Test
    fun getAllByFlowerbedId() {
        // prepare
        val flowerbedId = 1L
        val repositoryResult = listOf(
            FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = 2),
            FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = 1)
        )
        val expected = listOf(
            FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = 1),
            FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = 2)
        )

        every { flowerbedPhotoRepository.getAllByFlowerbedId(flowerbedId) } returns repositoryResult

        // do
        val actual = underTest.getAllByFlowerbedId(flowerbedId)

        // check
        verify { flowerbedPhotoRepository.getAllByFlowerbedId(flowerbedId) }
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun insertFlowerbedPhoto() {
        // prepare
        val flowerbedPhoto = FlowerbedPhoto(flowerbedId = 1, flowerbedPhotoId = null, order = 2)

        // check
        verify { flowerbedPhotoRepository.insertFlowerbedPhoto(flowerbedPhoto) }
    }

    @Test
    fun deleteFlowerbedPhoto() {
        // prepare
        val flowerbedPhoto = listOf(
            FlowerbedPhoto(
                flowerbedId = 1, flowerbedPhotoId = 1
            ),
            FlowerbedPhoto(flowerbedId = 1, flowerbedPhotoId = 1)
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

        // do
        underTest.getFlowerbedDir(flowerbedId)

        // check
        verify { dirRepository.getFlowerbedDir(flowerbedId) }
    }

}