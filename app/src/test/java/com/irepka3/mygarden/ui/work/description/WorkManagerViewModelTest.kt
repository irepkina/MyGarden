package com.irepka3.mygarden.ui.work.description

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.irepka3.mygarden.common.rules.RxSchedulersRule
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractor
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Schedule
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
import com.irepka3.mygarden.domain.util.date.toDate
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkTypeUI
import com.irepka3.mygarden.ui.work.description.model.WorkUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkUIState
import com.irepka3.mygarden.ui.work.model.WorkUIId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Тесты для вью-модели WorkManagerViewModel
 *
 * Created by i.repkina on 23.11.2021.
 */
class WorkManagerViewModelTest {
    @get:Rule
    val vmRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = RxSchedulersRule()

    private val interactor = mockk<WorkManagerInteractor>(relaxed = true)
    private val underTest = WorkManagerViewModel(interactor)
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    @Test
    fun onCreateViewNewWork() {
        // prepare
        val workUIId = WorkUIId(workId = null, repeatWorkId = null, planDate = null)
        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = null,
            repeatWorkId = null,
            name = "",
            description = "",
            datePlan = null,
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = true
        )

        val workUIState = WorkUIState(
            workTitle = "",
            workTypeUI = WorkTypeUI.NEW_WORK,
            isEditMode = false,
            isRepeatSwitcherEnabled = true,
            isDoneEnabled = false,
            isCancelEnabled = false,
            isClearStatusEnabled = false,
            isShowOriginRepeatEnabled = false,
            isReadOnly = false,
            isCommentReadonly = false
        )

        // do
        underTest.onCreateView(workUIId)

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(workUIMModel, underTest.workLiveData.value)
        assertEquals(workUIState, underTest.workUIStateLiveData.value)
        assertEquals(emptyList<ScheduleUIModel>(), underTest.scheduleLiveData.value)
    }

    @Test
    fun onCreateViewGenerateRepeatWork() {
        // prepare
        val planDate = 2L
        val workUIId = WorkUIId(workId = null, repeatWorkId = 1L, planDate = planDate)
        val work = Work(
            datePlan = planDate,
            repeatWork = RepeatWork(
                repeatWorkId = 1L,
                notificationDay = null,
                notificationHour = null,
                notificationMinute = null,
                noNotification = true
            )
        )

        every { interactor.getById(workUIId.workId, workUIId.repeatWorkId) } returns work

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = null,
            repeatWorkId = 1L,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = true
        )

        val workUIState = WorkUIState(
            workTitle = "",
            workTypeUI = WorkTypeUI.GENERATED_REPEAT,
            isEditMode = true,
            isRepeatSwitcherEnabled = false,
            isDoneEnabled = true,
            isCancelEnabled = true,
            isClearStatusEnabled = false,
            isShowOriginRepeatEnabled = true,
            isReadOnly = true,
            isCommentReadonly = false
        )

        // do
        underTest.onCreateView(workUIId)

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(workUIMModel, underTest.workLiveData.value)
        assertEquals(workUIState, underTest.workUIStateLiveData.value)
        assertEquals(emptyList<ScheduleUIModel>(), underTest.scheduleLiveData.value)
    }

    @Test
    fun onCreateViewOriginRepeatWork() {
        // prepare
        val planDate = 2L
        val schedules = listOf(Schedule(1L, 1L), Schedule(1L, 1L))
        val schedulesUIModelList = listOf(
            ScheduleUIModel(Schedule(1L, 1L), 0),
            ScheduleUIModel(Schedule(1L, 1L), 1)
        )
        val workUIId = WorkUIId(workId = 1L, repeatWorkId = 1L, planDate = planDate)
        val work = Work(
            datePlan = null,
            repeatWork = RepeatWork(
                repeatWorkId = 1L,
                notificationDay = null,
                notificationHour = null,
                notificationMinute = null,
                noNotification = true,
                schedules = schedules
            )
        )

        every { interactor.getById(workUIId.workId, workUIId.repeatWorkId) } returns work

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = null,
            repeatWorkId = 1L,
            name = "",
            description = "",
            datePlan = null,
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = true
        )

        val workUIState = WorkUIState(
            workTitle = "",
            workTypeUI = WorkTypeUI.GENERATED_REPEAT,
            isEditMode = true,
            isRepeatSwitcherEnabled = false,
            isDoneEnabled = true,
            isCancelEnabled = true,
            isClearStatusEnabled = false,
            isShowOriginRepeatEnabled = true,
            isReadOnly = true,
            isCommentReadonly = false
        )

        // do
        underTest.onCreateView(workUIId)

        // check
        assertEquals(false, underTest.progressLiveData.value)
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(workUIMModel, underTest.workLiveData.value)
        assertEquals(workUIState, underTest.workUIStateLiveData.value)
        assertEquals(schedulesUIModelList, underTest.scheduleLiveData.value)
    }

    @Test
    fun onDoneOnceWork() {
        // prepare
        val planDate = 1L
        val workId = 1L

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = workId,
            repeatWorkId = null,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        val work = Work(
            workId = workId,
            repeatWork = RepeatWork(),
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)).toDate(),
            dateDone = null,
            status = WorkStatus.Done,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        every { interactor.update(work) } returns Unit

        // do
        underTest.onDone(workUIMModel)

        // check
        verify { interactor.update(work) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(Command.CLOSE_VIEW, underTest.commandLiveData.value)


    }

    @Test
    fun onDoneRepeatWork() {
        // prepare
        val planDate = 1L
        val repeatWorkId = 1L

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = null,
            repeatWorkId = repeatWorkId,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        val work = Work(
            workId = null,
            repeatWork = RepeatWork(repeatWorkId = repeatWorkId),
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)).toDate(),
            dateDone = null,
            status = WorkStatus.Done,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        every { interactor.update(work) } returns Unit

        // do
        underTest.onDone(workUIMModel)

        // check
        verify { interactor.update(work) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(Command.CLOSE_VIEW, underTest.commandLiveData.value)
    }

    @Test
    fun onCancelOnceWork() {
        // prepare
        val planDate = 1L
        val workId = 1L

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = workId,
            repeatWorkId = null,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        val work = Work(
            workId = workId,
            repeatWork = RepeatWork(),
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)).toDate(),
            dateDone = null,
            status = WorkStatus.Cancel,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        every { interactor.update(work) } returns Unit

        // do
        underTest.onCancel(workUIMModel)

        // check
        verify { interactor.update(work) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(Command.CLOSE_VIEW, underTest.commandLiveData.value)


    }

    @Test
    fun onCancelRepeatWork() {
        // prepare
        val planDate = 1L
        val repeatWorkId = 1L

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = null,
            repeatWorkId = repeatWorkId,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        val work = Work(
            workId = null,
            repeatWork = RepeatWork(repeatWorkId = repeatWorkId),
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)).toDate(),
            dateDone = null,
            status = WorkStatus.Cancel,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        every { interactor.update(work) } returns Unit

        // do
        underTest.onCancel(workUIMModel)

        // check
        verify { interactor.update(work) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(Command.CLOSE_VIEW, underTest.commandLiveData.value)
    }

    @Test
    fun onClearOnceWork() {
        // prepare
        val planDate = 1L
        val workId = 1L

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = workId,
            repeatWorkId = null,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        val work = Work(
            workId = workId,
            repeatWork = RepeatWork(),
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)).toDate(),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        every { interactor.update(work) } returns Unit

        // do
        underTest.onClear(workUIMModel)

        // check
        verify { interactor.update(work) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(Command.CLOSE_VIEW, underTest.commandLiveData.value)


    }

    @Test
    fun onClearRepeatWork() {
        // prepare
        val planDate = 1L
        val repeatWorkId = 1L

        val workUIMModel = WorkUIModel(
            isOnceWork = false,
            workId = null,
            repeatWorkId = repeatWorkId,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        val work = Work(
            workId = null,
            repeatWork = RepeatWork(repeatWorkId = repeatWorkId),
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)).toDate(),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        every { interactor.update(work) } returns Unit

        // do
        underTest.onClear(workUIMModel)

        // check
        verify { interactor.update(work) }
        assertEquals(null, underTest.errorsLiveData.value)
        assertEquals(Command.CLOSE_VIEW, underTest.commandLiveData.value)
    }

    @Test
    fun onDataChanged() {
        //do
        underTest.onDataChanged()

        // check
        assertEquals(true, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onStoreData() {
        // prepare
        val planDate = 1L
        val workId = 1L
        val expected = WorkUIModel(
            isOnceWork = false,
            workId = workId,
            repeatWorkId = null,
            name = "",
            description = "",
            datePlan = dateFormat.format(Date(planDate)),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )

        // do
        underTest.onStoreData(expected)

        // check
        assertEquals(expected, underTest.workLiveData.value)
    }

    @Test
    fun onUpdateSchedule() {
        // prepare
        val scheduleData = ScheduleUIModel(
            Schedule(
                scheduleId = 1L,
                repeatWorkId = 1L
            ),
            1
        )
        val expected = listOf(scheduleData)

        // do
        underTest.onUpdateSchedule(scheduleData)

        // check
        assertEquals(expected, underTest.scheduleLiveData.value)
        assertEquals(true, underTest.isDataChangedLiveData.value)
    }

    @Test
    fun onDeleteSchedule() {
        // prepare
        val planDate = 2L
        val schedules = listOf(Schedule(1L, 1L), Schedule(1L, 1L))
        val workUIId = WorkUIId(workId = 1L, repeatWorkId = 1L, planDate = planDate)
        val work = Work(
            datePlan = null,
            repeatWork = RepeatWork(
                repeatWorkId = 1L,
                notificationDay = null,
                notificationHour = null,
                notificationMinute = null,
                noNotification = true,
                schedules = schedules
            )
        )
        every { interactor.getById(workUIId.workId, workUIId.repeatWorkId) } returns work
        underTest.onCreateView(workUIId)
        val scheduleData = ScheduleUIModel(Schedule(1L, 1L), 1)
        val expected = listOf(ScheduleUIModel(Schedule(1L, 1L), 0))

        // do
        underTest.onDeleteSchedule(scheduleData)

        // check
        assertEquals(expected, underTest.scheduleLiveData.value)
        assertEquals(true, underTest.isDataChangedLiveData.value)
    }
}