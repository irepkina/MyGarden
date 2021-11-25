package com.irepka3.mygarden.ui.flowerbed.plants.description

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.PlantFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Тесты для класса PlantDescriptionViewModel
 *
 * Created by i.repkina on 23.11.2021.
 */
class PlantDescriptionViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val interactor = mockk<PlantInteractor>(relaxUnitFun = true)


    @Test
    fun onSaveDataInsert() {
        //prepare
        val flowerbedId = 1L
        val plantId = 1L
        val newPlant = PlantFactory.createPlantEmptyUri(flowerbedId, null)
        val plant = PlantFactory.createPlantEmptyUri(flowerbedId, plantId)
        every { interactor.insertPlant(newPlant) } returns plantId
        every { interactor.updatePlant(plant) } returns Unit
        val underTest = PlantDescriptionViewModel(flowerbedId, null, interactor)

        //do
        underTest.onSaveData(
            flowerbedId = flowerbedId,
            plantId = null,
            name = plant.name,
            description = plant.description,
            comment = plant.comment,
            count = 1,
            plantDate = null
        )

        // check
        verify(exactly = 0) { interactor.updatePlant(plant) }
        verify { interactor.insertPlant(newPlant) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(plant, underTest.plantLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onSaveDataUpdate() {
        //prepare
        val flowerbedId = 1L
        val plantId = 1L
        val newPlant = PlantFactory.createPlantEmptyUri(flowerbedId, null)
        val plant = PlantFactory.createPlantEmptyUri(flowerbedId, plantId)
        every { interactor.insertPlant(newPlant) } returns plantId
        every { interactor.updatePlant(plant) } returns Unit
        val underTest = PlantDescriptionViewModel(flowerbedId, null, interactor)

        //do
        underTest.onSaveData(
            flowerbedId = flowerbedId,
            plantId = plantId,
            name = plant.name,
            description = plant.description,
            comment = plant.comment,
            count = 1,
            plantDate = null
        )

        // check
        verify { interactor.updatePlant(plant) }
        verify(exactly = 0) { interactor.insertPlant(newPlant) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(plant, underTest.plantLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onStoreData() {
        //prepare
        val flowerbedId = 1L
        val plantId = 1L
        val plant = PlantFactory.createPlantEmptyUri(flowerbedId, plantId)
        every { interactor.getPlant(flowerbedId) } returns plant
        val underTest = PlantDescriptionViewModel(flowerbedId, plantId, interactor)

        //do
        underTest.onStoreData(
            flowerbedId = flowerbedId,
            plantId = plantId,
            name = plant.name,
            description = plant.description,
            comment = plant.comment,
            count = 1,
            plantDate = null
        )

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(plant, underTest.plantLiveData.value)
    }

    @Test
    fun onStoreDataEmptyPlantId() {
        //prepare
        val flowerbedId = 1L
        val plantId = null
        val plant = PlantFactory.createPlantEmpty(flowerbedId)
        every { interactor.getPlant(flowerbedId) } returns plant
        val underTest = PlantDescriptionViewModel(flowerbedId, plantId, interactor)

        //do
        underTest.onStoreData(
            flowerbedId = flowerbedId,
            plantId = plantId,
            name = plant.name,
            description = plant.description,
            comment = plant.comment,
            count = 1,
            plantDate = null
        )

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(plant, underTest.plantLiveData.value)
    }

    @Test
    fun onDataChanged() {
        //prepare
        val flowerbedId = 1L
        val plantId = null
        val underTest = PlantDescriptionViewModel(flowerbedId, plantId, interactor)

        //do
        underTest.onDataChanged()
        // check
        assertEquals(true, underTest.isDataChangedLiveData.value)
    }
}