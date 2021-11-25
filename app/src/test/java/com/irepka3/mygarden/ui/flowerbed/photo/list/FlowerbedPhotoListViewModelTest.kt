package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.PhotoFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Тесты для вью-модели FlowerbedPhotoListViewModel
 *
 * Created by i.repkina on 23.11.2021.
 */
class FlowerbedPhotoListViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val flowerbedPhotoInteractor = mockk<FlowerbedPhotoInteractor>(relaxed = true)
    private val fileInteractor = mockk<FileInteractor>(relaxed = true)
    private val flowerbedId = 1L
    private lateinit var underTest: FlowerbedPhotoListViewModel
    private val photoDir = mockk<File>()
    private val uri = mockk<Uri>()

    private val photoId = 1L
    private val photoList = listOf(
        PhotoFactory.createPhoto(1L, "uri1", true),
        PhotoFactory.createPhoto(2L, "uri2", false)
    )
    private val flowerbedPhoto = listOf(
        FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = photoId, "uri1", true),
        FlowerbedPhoto(flowerbedId = flowerbedId, flowerbedPhotoId = 2L, "uri2", false)
    )

    @Before
    fun beforeTest() {
        every { flowerbedPhotoInteractor.getAllByFlowerbedId(flowerbedId) } returns flowerbedPhoto
        underTest =
            FlowerbedPhotoListViewModel(flowerbedId, fileInteractor, flowerbedPhotoInteractor)
    }


    @Test
    fun loadData() {
        // check
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(photoList, underTest.photoListLiveData.value)
    }

    @Test
    fun onInsert() {
        // prepare
        every { fileInteractor.copyFileToLocalStorage(photoDir, uri) } returns uri
        every { flowerbedPhotoInteractor.getFlowerbedDir(flowerbedId) } returns photoDir

        // do
        underTest.onInsert(uri)

        // check
        verify { fileInteractor.copyFileToLocalStorage(photoDir, uri) }
        verify { flowerbedPhotoInteractor.getFlowerbedDir(flowerbedId) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(photoList, underTest.photoListLiveData.value)
    }

    @Test
    fun onDelete() {
        // prepare
        every { flowerbedPhotoInteractor.deleteFlowerbedPhoto(flowerbedPhoto) } returns Unit

        // do
        underTest.onDelete(photoList)

        // check
        verify { flowerbedPhotoInteractor.deleteFlowerbedPhoto(flowerbedPhoto) }
        verify { flowerbedPhotoInteractor.getAllByFlowerbedId(flowerbedId) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(photoList, underTest.photoListLiveData.value)
    }


    @Test
    fun onSelected() {
        // prepare
        every {
            flowerbedPhotoInteractor.updateSelectedPhoto(
                flowerbedId = flowerbedId,
                flowerbedPhotoId = photoId
            )
        } returns Unit

        // do
        underTest.onSelected(photoId)

        // check
        verify {
            flowerbedPhotoInteractor.updateSelectedPhoto(
                flowerbedId = flowerbedId,
                flowerbedPhotoId = photoId
            )
        }
        verify { flowerbedPhotoInteractor.getAllByFlowerbedId(flowerbedId) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(photoList, underTest.photoListLiveData.value)
    }

    @After
    fun afterTest() {
        assertEquals(false, underTest.progressLiveData.value)
    }

}
