package com.irepka3.mygarden.ui.work.description

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractor
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
import com.irepka3.mygarden.domain.util.date.toDate
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkUIState
import com.irepka3.mygarden.ui.work.description.model.toUIModel
import com.irepka3.mygarden.ui.work.description.model.toWorkUIState
import com.irepka3.mygarden.ui.work.model.WorkUIId
import com.irepka3.mygarden.util.Const
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Вью модель для экрана работы
 *
 * Created by i.repkina on 11.11.2021.
 */
class WorkManagerViewModel(
    private val interactor: WorkManagerInteractor
) : ViewModel() {

    private val _workLiveData = MutableLiveData<WorkUIModel>()

    /**
     * LiveData работы
     */
    val workLiveData: LiveData<WorkUIModel> = _workLiveData

    private val _scheduleLiveData = MutableLiveData<MutableList<ScheduleUIModel>>()

    /**
     * LiveData расписания
     */
    val scheduleLiveData: LiveData<MutableList<ScheduleUIModel>> = _scheduleLiveData

    private val _workUIStateLiveData = MutableLiveData<WorkUIState>()

    /**
     * LiveData работы
     */
    val workUIStateLiveData: LiveData<WorkUIState> = _workUIStateLiveData

    private val _errorsLiveData = MutableLiveData<Throwable>()

    /**
     * LiveData для вывода ошибки
     */
    val errorsLiveData: LiveData<Throwable> = _errorsLiveData

    private val _progressLiveData = MutableLiveData<Boolean>()

    /**
     * LiveData для отображения индикатора загрузки данных
     */
    val progressLiveData: LiveData<Boolean> = _progressLiveData

    private val _isDataChangedLiveData = MutableLiveData<Boolean>()

    /**
     * LiveData для отслеживания изменения данных на экране
     */
    val isDataChangedLiveData: LiveData<Boolean> = _isDataChangedLiveData


    private val _commandLiveData = MutableLiveData(Command.NO_COMMAND)

    /**
     * LiveData возвращающая команды для вью
     */
    val commandLiveData: LiveData<Command> = _commandLiveData

    private var isLoaded: Boolean = false

    private val compositeDisposable = CompositeDisposable()

    /**
     *  Событие создания вью
     *  @param workUIId идентификатор работы [WorkUIId]
     */
    fun onCreateView(workUIId: WorkUIId) {
        if (!isLoaded) {
            loadData(workUIId)
        }
    }

    /**
     * Загрузка данных во view-модель
     *  @param workUIId идентификатор работы [WorkUIId]
     */
    private fun loadData(workUIId: WorkUIId) {
        Log.d(TAG, "loadData() called")
        _progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable {
                when {
                    workUIId.workId == null && workUIId.repeatWorkId == null -> {
                        Work(
                            repeatWork = RepeatWork(
                                notificationDay = null,
                                notificationHour = null,
                                notificationMinute = null,
                                noNotification = true
                            )
                        )
                    }
                    else -> {
                        val work = interactor.getById(workUIId.workId, workUIId.repeatWorkId)
                        // если сгенерированная работа, то копируем дату из параметров
                        if (workUIId.workId == null) {
                            work.copy(datePlan = workUIId.planDate)
                        } else work
                    }
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    { work ->
                        _isDataChangedLiveData.value = false
                        _workLiveData.value = work.toUIModel()
                        _workUIStateLiveData.value = work.toWorkUIState()
                        _scheduleLiveData.value = work?.repeatWork?.schedules
                            ?.mapIndexed { index, schedule -> schedule.toUIModel(index) }
                            ?.toMutableList()
                        isLoaded = true
                    },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }

    /**
     * Очищаем подписки на rx при удалении вью-модели
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * Отметка выполнения работы
     * @param workData данные формы [WorkUIModel]
     */
    fun onDone(workData: WorkUIModel) {
        workData.status = WorkStatus.Done
        SaveData(workData)
    }

    /**
     * Отмена работы
     * @param workData данные формы [WorkUIModel]
     */
    fun onCancel(workData: WorkUIModel) {
        workData.status = WorkStatus.Cancel
        SaveData(workData)
    }

    /**
     * Очистить статус работы (изменить на План)
     * @param workData данные формы [WorkUIModel]
     */
    fun onClear(workData: WorkUIModel) {
        workData.status = WorkStatus.Plan
        SaveData(workData)
    }


    /**
     * Соханить данные работы
     * @param workData данные формы [WorkUIModel]
     */
    fun onSaveData(workData: WorkUIModel) {
        SaveData(workData)
    }

    /**
     * Сохранение данных работы
     * @param workData данные формы [WorkUIModel]
     */
    private fun SaveData(workData: WorkUIModel) {
        Log.d(TAG, "onSaveData() called, workData = $workData")
        val work = createWorkItem(workData)
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                when {
                    // новая запись
                    work.workId == null && work.repeatWork?.repeatWorkId == null -> {
                        interactor.insert(workData.isOnceWork, work)
                    }
                    else -> {
                        interactor.update(work)
                    }
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    {

                        _workLiveData.value = work.toUIModel()
                        _workUIStateLiveData.value = work.toWorkUIState()
                        _commandLiveData.value = Command.CLOSE_VIEW
                        _isDataChangedLiveData.value = false
                    },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }

    /**
     * Сохраняет данные экрана во вью-модель
     * @param workData данные формы [WorkUIModel]
     */
    fun onStoreData(workData: WorkUIModel) {
        _workLiveData.value = workData
    }

    /**
     * Обновляет данные расписания во вью-модели
     * @param scheduleData данные расписания [ScheduleUIModel]
     */
    fun onUpdateSchedule(scheduleData: ScheduleUIModel) {
        val schedules = _scheduleLiveData.value ?: mutableListOf()
        val index = schedules.indexOfFirst { it.number == scheduleData.number }
        if (index != -1) {
            schedules[index] = scheduleData
        } else {
            scheduleData.number = schedules.size
            schedules.add(scheduleData)
        }
        _scheduleLiveData.value = schedules
        _isDataChangedLiveData.value = true
    }

    /**
     * Удаляет расписание из вью-модели
     * @param scheduleData данные расписания [ScheduleUIModel]
     */
    fun onDeleteSchedule(scheduleData: ScheduleUIModel) {
        val schedules = _scheduleLiveData.value
        Log.d(TAG, "onDeleteSchedule() called with: scheduleData = $scheduleData")
        schedules?.let {
            it.removeIf {
                Log.d(TAG, "onDeleteSchedule(), it.number = ${it.number} called")
                it.number == scheduleData.number
            }
            _scheduleLiveData.value = it
        }
        _isDataChangedLiveData.value = true
    }

    /**
     * Создает Work по данным вью
     * @param workData данные формы [WorkUIModel]
     */
    private fun createWorkItem(workData: WorkUIModel): Work {
        Log.d(TAG, "createWorkItem() called")
        val workDataPlan = workData.datePlan

        val datePlan = workDataPlan.toDate { error ->
            _errorsLiveData.value = error
        }

        val dateDone = workData.dateDone.toDate { error ->
            _errorsLiveData.value = error
        }
        val repeatWork = if (!workData.isOnceWork || workData.repeatWorkId != null) {
            RepeatWork(
                repeatWorkId = workData.repeatWorkId,
                name = workData.name ?: "",
                description = workData.description,
                notificationDay = workData.notificationDay,
                notificationHour = workData.notificationHour,
                notificationMinute = workData.notificationMinute,
                noNotification = workData.noNotification,
                status = if (workData.repeatWorkId == workData.workId) workData.status else WorkStatus.Plan,
                schedules = _scheduleLiveData.value?.map { it.schedule } ?: emptyList()
            )
        } else null

        return Work(
            workId = workData.workId,
            repeatWork = repeatWork,
            name = workData.name ?: "",
            description = workData.description,
            datePlan = datePlan,
            dateDone = dateDone,
            status = workData.status,
            notificationDay = workData.notificationDay,
            notificationHour = workData.notificationHour,
            notificationMinute = workData.notificationMinute,
            noNotification = workData.noNotification
        )
    }

    /**
     * Сохранение в лайвдату информации об изменении данных на экране
     */
    fun onDataChanged() {
        _isDataChangedLiveData.value = true
    }
}

/**
 * Команда закрыть вью
 */
enum class Command {
    NO_COMMAND, CLOSE_VIEW
}

private const val TAG = "${Const.APP_TAG}.WorkManagerViewModel"