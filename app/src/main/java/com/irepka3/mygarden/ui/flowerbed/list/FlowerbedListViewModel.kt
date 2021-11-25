package com.irepka3.mygarden.ui.flowerbed.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.util.Const
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * View-модель для фрагмента списка клумб
 * @param interactor доменного слоя для работы с клумбами [FlowerbedInteractor]
 *
 * Created by i.repkina on 01.11.2021.
 */
class FlowerbedListViewModel(
    private val interactor: FlowerbedInteractor
) : ViewModel() {

    private val _flowerbedLiveData = MutableLiveData<List<Flowerbed>>()

    /**
     * LiveData списка клумб
     */
    val flowerbedLiveData: LiveData<List<Flowerbed>> = _flowerbedLiveData

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

    private val compositeDisposable = CompositeDisposable()

    /**
     * Загрузка данных во view-модель
     */
    fun loadData() {
        Log.d(TAG, "loadData() called")
        _progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable {
                interactor.getFlowerbedAll()
                    .also { Log.d(TAG, "loadData.getFlowerbedAll finished") }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    { list -> _flowerbedLiveData.value = list },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }


    /**
     * Удаление выбранной клумбы
     * @param flowerbed выбранная клумба [Flowerbed]
     */
    fun onDelete(flowerbed: Flowerbed) {
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                if (flowerbed.flowerbedId == null) {
                    throw IllegalStateException("Invalid flowerbedId, flowerbedId is null")
                }
                interactor.deleteFlowerbed(flowerbed)
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
     * Очищаем подписки на rx при удалении вью-модели
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

private const val TAG = "${Const.APP_TAG}.FlowerbedListViewModel"