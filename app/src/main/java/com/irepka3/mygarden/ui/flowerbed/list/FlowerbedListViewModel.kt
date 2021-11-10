package com.irepka3.mygarden.ui.flowerbed.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.interactor.FileInteractor
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
    ): ViewModel() {
    /**
     * LiveData списка клумб
     */
    val flowerbedLiveData = MutableLiveData<List<Flowerbed>>()
    /**
     * LiveData для вывода ошибки
     */
    val errorsLiveData = MutableLiveData<Throwable>()
    /**
     * LiveData для отображения индикатора загрузки данных
     */
    val progressLiveData = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Загрузка данных во view-модель
     */
    fun loadData(){
        Log.d(TAG, "loadData() called")
        progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable {
                interactor.getFlowerbedAll()
                    .also{ Log.d(TAG, "loadData.getFlowerbedAll finished") }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressLiveData.value = false }
                .subscribe(
                    { list -> flowerbedLiveData.value = list },
                    { error ->  errorsLiveData.value = error }
                )
        )
    }


    /**
     * Удаление выбранной клумбы
     * @param flowerbed выбранная клумба [Flowerbed]
     */
    fun onDelete(flowerbed: Flowerbed){
        progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                if (flowerbed.flowerbedId == null){
                    throw Exception("Invalid flowerbedId, flowerbedId is null")
                }
                interactor.deleteFlowerbed(flowerbed)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressLiveData.value = false }
                .subscribe(
                    {
                        loadData()
                    },
                    { error -> errorsLiveData.value = error }
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