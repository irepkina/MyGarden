package com.irepka3.mygarden.ui.flowerbed.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.factory.FlowerbedFactory
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Тесты для вью-модели FlowerbedListViewModel
 *
 * Created by i.repkina on 23.11.2021.
 */
class FlowerbedListViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val interactor = mockk<FlowerbedInteractor>(relaxed = true)
    private val underTest = FlowerbedListViewModel(interactor)
    private val flowerbed = FlowerbedFactory.createFlowerbed(1L)
    private val flowerbedList = listOf(
        flowerbed,
        FlowerbedFactory.createFlowerbed(2L)
    )

    @Before
    fun beforeTest() {
        every { interactor.getFlowerbedAll() } returns flowerbedList
        every { interactor.deleteFlowerbed(flowerbed) } returns Unit
    }

    @Test
    fun loadData() {
        // do
        underTest.loadData()

        // check
        verify { interactor.getFlowerbedAll() }
        Assert.assertEquals(null, underTest.errorsLiveData.value)
        Assert.assertEquals(flowerbedList, underTest.flowerbedLiveData.value)
    }

    @Test
    fun onDelete() {
        // do
        underTest.onDelete(flowerbed)

        // check
        verify { interactor.deleteFlowerbed(flowerbed) }
        Assert.assertEquals(null, underTest.errorsLiveData.value)
        Assert.assertEquals(flowerbedList, underTest.flowerbedLiveData.value)
    }

    @Test()
    fun onDeleteError() {
        // prepare
        val exception = IllegalStateException("Invalid flowerbedId, flowerbedId is null")
        val flowerbed = FlowerbedFactory.createFlowerbed(null)

        // do
        underTest.onDelete(flowerbed)

        // check
        verify(exactly = 0) { interactor.deleteFlowerbed(flowerbed) }
        Assert.assertEquals(exception.message, underTest.errorsLiveData.value?.message)
        Assert.assertEquals(null, underTest.flowerbedLiveData.value)
    }

    @After
    fun afterTest() {
        Assert.assertEquals(false, underTest.progressLiveData.value)
    }

}