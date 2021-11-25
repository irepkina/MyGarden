package com.irepka3.mygarden.ui.flowerbed.plants.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.PlantFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Тесты для вью модели PlantListViewModel
 *
 * Created by i.repkina on 23.11.2021.
 */
class PlantListViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val flowerbedId = 1L
    private val interactor = mockk<PlantInteractor>(relaxed = true)
    private val underTest = PlantListViewModel(flowerbedId, interactor)
    val plant = PlantFactory.createPlantEmptyUri(flowerbedId, 1L)
    private val plantsList = listOf(
        plant,
        PlantFactory.createPlantEmptyUri(flowerbedId, 2L)
    )

    @Before
    fun beforeTest() {
        every { interactor.getPlantsByFlowerbed(flowerbedId) } returns plantsList
        every { interactor.deletePlant(plant) } returns Unit
    }

    @Test
    fun onCreateView() {
        // do
        underTest.onCreateView()

        // check
        verify { interactor.getPlantsByFlowerbed(flowerbedId) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(plantsList, underTest.plantListLiveData.value)
    }

    @Test
    fun onDelete() {
        // do
        underTest.onDelete(plant)

        // check
        verify { interactor.deletePlant(plant) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(plantsList, underTest.plantListLiveData.value)
    }


    @After
    fun afterTest() {
        assertEquals(false, underTest.progressLiveData.value)
    }
}