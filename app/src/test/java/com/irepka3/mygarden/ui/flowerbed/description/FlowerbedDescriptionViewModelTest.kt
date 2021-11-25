package com.irepka3.mygarden.ui.flowerbed.description

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.FlowerbedFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Тесты для вью модели FlowerbedDescriptionViewModel
 *
 * Created by i.repkina on 21.11.2021.
 */
class FlowerbedDescriptionViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val interactor = mockk<FlowerbedInteractor>(relaxUnitFun = true)

    @Test
    fun loadDataEmptyFlowerbedId() {
        //prepare
        val flowerbedIdEmpty = null
        val flowerbedId = 1L
        val flowerbed = FlowerbedFactory.createFlowerbed(flowerbedId)
        val flowerbedEmpty = FlowerbedFactory.createFlowerbedEmpty()
        every { interactor.getFlowerbed(flowerbedId) } returns flowerbed

        // do
        val underTest = FlowerbedDescriptionViewModel(flowerbedIdEmpty, interactor)

        // check
        verify(exactly = 0) { interactor.getFlowerbed(flowerbedId) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(flowerbedEmpty, underTest.flowerbedLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }


    @Test
    fun loadData() {
        //prepare
        val flowerbedId = 1L
        val flowerbed = FlowerbedFactory.createFlowerbedEmptyUri(flowerbedId)
        every { interactor.getFlowerbed(flowerbedId) } returns flowerbed

        // do
        val underTest = FlowerbedDescriptionViewModel(flowerbedId, interactor)

        // check
        verify { interactor.getFlowerbed(flowerbedId) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(flowerbed, underTest.flowerbedLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun loadDataError() {
        //prepare
        val flowerbedId = 1L
        val expectedExection = Exception()
        every { interactor.getFlowerbed(flowerbedId) } throws expectedExection

        //do
        val underTest = FlowerbedDescriptionViewModel(flowerbedId, interactor)

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(expectedExection, underTest.errorsLiveData.value)
        assertEquals(null, underTest.flowerbedLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onSaveDataInsert() {
        //prepare
        val flowerbedId = 1L
        val newFlowerbed = FlowerbedFactory.createFlowerbedEmptyUri(null)
        val flowerbed = FlowerbedFactory.createFlowerbedEmptyUri(flowerbedId)
        every { interactor.insertFlowerbed(newFlowerbed) } returns flowerbedId
        every { interactor.updateFlowerbed(flowerbed) } returns Unit
        val underTest = FlowerbedDescriptionViewModel(null, interactor)

        //do
        underTest.onSaveData(
            flowerbedId = newFlowerbed.flowerbedId,
            name = newFlowerbed.name,
            description = newFlowerbed.description,
            comment = newFlowerbed.comment
        )

        // check
        verify(exactly = 0) { interactor.updateFlowerbed(flowerbed) }
        verify { interactor.insertFlowerbed(newFlowerbed) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(flowerbed, underTest.flowerbedLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onSaveDataUpdate() {
        //prepare
        val flowerbedId = 1L
        val flowerbed = FlowerbedFactory.createFlowerbedEmptyUri(flowerbedId)
        every { interactor.getFlowerbed(flowerbedId) } returns flowerbed
        every { interactor.updateFlowerbed(flowerbed) } returns Unit
        val underTest = FlowerbedDescriptionViewModel(flowerbedId, interactor)

        //do
        underTest.onSaveData(
            flowerbedId = flowerbed.flowerbedId,
            name = flowerbed.name,
            description = flowerbed.description,
            comment = flowerbed.comment
        )

        // check
        verify { interactor.updateFlowerbed(flowerbed) }
        verify(exactly = 0) { interactor.insertFlowerbed(flowerbed) }
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(flowerbed, underTest.flowerbedLiveData.value)
        assertEquals(false, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onStoreData() {
        //prepare
        val flowerbedId = 1L
        val flowerbed = FlowerbedFactory.createFlowerbedEmptyUri(flowerbedId)
        every { interactor.getFlowerbed(flowerbedId) } returns flowerbed
        val underTest = FlowerbedDescriptionViewModel(flowerbedId, interactor)

        //do
        underTest.onStoreData(
            flowerbedId = flowerbed.flowerbedId,
            name = flowerbed.name,
            description = flowerbed.description,
            comment = flowerbed.comment
        )

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(flowerbed, underTest.flowerbedLiveData.value)
    }

    @Test
    fun onDataChanged() {
        //prepare
        val flowerbedId = 1L
        val underTest = FlowerbedDescriptionViewModel(flowerbedId, interactor)
        //do
        underTest.onDataChanged()
        // check
        assertEquals(true, underTest.isDataChangedLiveData.value)
    }
}