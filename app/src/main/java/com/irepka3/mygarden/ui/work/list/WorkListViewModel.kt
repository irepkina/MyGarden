package com.irepka3.mygarden.ui.work.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractor
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.util.Const
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate

/**
 * View-модель для фрагмента списка работ
 * @param interactor доменного слоя для работы с однократными и повторяющимися работами [WorkManagerInteractor]
 *
 * Created by i.repkina on 10.11.2021.
 */
class WorkListViewModel(
    private val interactor: WorkManagerInteractor
) : ViewModel() {

    private val _workLiveData = MutableLiveData<List<Work>>()

    /**
     * LiveData списка работ
     */
    val workLiveData: LiveData<List<Work>> = _workLiveData

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

    private val _periodLiveData = MutableLiveData<LocalDate>()

    /**
     * LiveData для периода загрузки данных (дата)
     */
    val periodLiveData: LiveData<LocalDate> = _periodLiveData

    private val compositeDisposable = CompositeDisposable()

    /**
     * Событие создания вью
     */
    fun onCreateView() {
        if (_periodLiveData.value == null) {
            val currentDate = LocalDate.now()
            val year = currentDate.year
            val month = currentDate.month.value
            _periodLiveData.value = LocalDate.of(year, month, 1)
        }
        loadData()
    }

    /**
     * Загрузка данных во view-модель
     */
    fun loadData() {
        Log.d(TAG, "loadData() called")
        val currentDate = LocalDate.now()
        val year = periodLiveData.value?.year ?: currentDate.year
        val month = periodLiveData.value?.monthValue ?: currentDate.monthValue
        _progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable {
                interactor.getAll(year, month)
                    .also { Log.d(TAG, "loadData.getAll finished") }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    { list -> _workLiveData.value = list },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }


    /**
     * Удаление выбранной работы
     * @param work выбранная работа [Work]
     */
    fun onDelete(work: Work) {
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                val isOnceWork = (work.workId != null)
                if (work.workId == null && work.repeatWork?.repeatWorkId == null) {
                    throw IllegalStateException("Invalid workId, workId is null")
                }
                interactor.delete(isOnceWork, work)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    {
                        loadData()
                    },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }

    /**
     * Сдвигает период для загрузки данных на месяц вперед
     */
    fun onNextClick() {
        _periodLiveData.value = _periodLiveData.value?.plusMonths(1)
    }

    /**
     * Сдвигает период для загрузки данных на месяц назад
     */
    fun onPreviousClick() {
        _periodLiveData.value = _periodLiveData.value?.plusMonths(-1)
    }

    /**
     * Очищаем подписки на rx при удалении вью-модели
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}


private const val TAG = "${Const.APP_TAG}.WorkListViewModel"