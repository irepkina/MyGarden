package com.irepka3.mygarden.ui.flowerbed.plants.photo.list

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.PhotoFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.PlantPhotoInteractor
import com.irepka3.mygarden.domain.model.PlantPhoto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Created by i.repkina on 24.11.2021.
 */
class PlantPhotoListViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val flowerbedId = 1L
    private val plantPhotoInteractor = mockk<PlantPhotoInteractor>(relaxed = true)
    private val fileInteractor = mockk<FileInteractor>(relaxed = true)
    private val plantId = 1L
    private lateinit var underTest: PlantPhotoListViewModel
    private val photoDir = mockk<File>()
    private val uri = mockk<Uri>()

    private val photoId = 1L
    private val photoList = listOf(
        PhotoFactory.createPhoto(1L, "uri1", true),
        PhotoFactory.createPhoto(2L, "uri2", false)
    )
    private val plantPhoto = listOf(
        PlantPhoto(plantId = plantId, plantPhotoId = photoId, "uri1", true),
        PlantPhoto(plantId = plantId, plantPhotoId = 2L, "uri2", false)
    )

    @Before
    fun beforeTest() {
        every { plantPhotoInteractor.getAllByPlantId(plantId) } returns plantPhoto
        underTest =
            PlantPhotoListViewModel(flowerbedId, plantId, fileInteractor, plantPhotoInteractor)
        underTest.onCreateView()
    }


    @Test
    fun onCreateView() {
        // check
        Assert.assertEquals(null, underTest.errorsLiveData.value)
        Assert.assertEquals(photoList, underTest.photoListLiveData.value)
    }

    @Test
    fun onInsert() {
        // prepare
        every { fileInteractor.copyFileToLocalStorage(photoDir, uri) } returns uri
        every { plantPhotoInteractor.getPlantDir(flowerbedId, plantId) } returns photoDir

        // do
        underTest.onInsert(uri)

        // check
        verify { fileInteractor.copyFileToLocalStorage(photoDir, uri) }
        verify { plantPhotoInteractor.getPlantDir(flowerbedId, plantId) }
        Assert.assertEquals(null, underTest.errorsLiveData.value)
        Assert.assertEquals(photoList, underTest.photoListLiveData.value)
    }

    @Test
    fun onDelete() {
        // prepare
        every { plantPhotoInteractor.deletePlantPhoto(plantPhoto) } returns Unit

        // do
        underTest.onDelete(photoList)

        // check
        verify { plantPhotoInteractor.deletePlantPhoto(plantPhoto) }
        verify { plantPhotoInteractor.getAllByPlantId(plantId) }
        Assert.assertEquals(null, underTest.errorsLiveData.value)
        Assert.assertEquals(photoList, underTest.photoListLiveData.value)
    }


    @Test
    fun onSelected() {
        // prepare
        every {
            plantPhotoInteractor.updateSelectedPhoto(plantId = plantId, plantPhotoId = photoId)
        } returns Unit

        // do
        underTest.onSelected(photoId)

        // check
        verify {
            plantPhotoInteractor.updateSelectedPhoto(
                plantId = plantId,
                plantPhotoId = photoId
            )
        }
        verify { plantPhotoInteractor.getAllByPlantId(plantId) }
        Assert.assertEquals(null, underTest.errorsLiveData.value)
        Assert.assertEquals(photoList, underTest.photoListLiveData.value)
    }

    @After
    fun afterTest() {
        Assert.assertEquals(false, underTest.progressLiveData.value)
    }
}