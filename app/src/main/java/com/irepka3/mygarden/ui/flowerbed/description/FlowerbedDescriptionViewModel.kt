package com.irepka3.mygarden.ui.flowerbed.description

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.util.Const.APP_TAG
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * View-модель для фрагмента карточки клумбы
 * @param flowerbedId идентификатор клумбы. Может быть null если запись еще не сохранена в базу
 * @param interactor интерактор доменного слоя для работы с клумбами [FlowerbedInteractor]
 *
 * Created by i.repkina on 01.11.2021.
 */
class FlowerbedDescriptionViewModel(
    private val flowerbedId: Long?,
    private val interactor: FlowerbedInteractor
) : ViewModel() {
    private val _flowerbedLiveData = MutableLiveData<Flowerbed>()

    /**
     * LiveData клумбы
     */
    val flowerbedLiveData: LiveData<Flowerbed> = _flowerbedLiveData

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
     * Загрузка данных во view-модель при создании
     */
    init {
        loadData()
    }

    /**
     * Загрузка данных во view-модель
     */
    private fun loadData(){
        _progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable {
                if (flowerbedId == null) {
                    Flowerbed()
                } else {
                    interactor.getFlowerbed(flowerbedId)
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    { flowerbed -> _flowerbedLiveData.value = flowerbed },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }

    /**
     * Обновление данных клумбы
     * @param flowerbedId идентификатор клумбы. Если в режиме вставке, то null
     * @param name имя клумба
     * @param description описание клумбы
     * @param comment комментарий к клумбе
     */
    fun onSaveData(flowerbedId: Long?, name: String, description: String, comment: String?){
        Log.d(TAG, "onSaveData() called with: id = $flowerbedId, name = $name, description = $description, comment = $comment")

        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                if (flowerbedId == null) {
                    Log.d(TAG, "onSaveData(), insert called")
                    val flowerbed = Flowerbed(null, name, description, comment, null)
                    val newFlowerbedId = interactor.insertFlowerbed(flowerbed)
                    _flowerbedLiveData.postValue(flowerbed.copy(flowerbedId = newFlowerbedId))
                } else {
                    Log.d(TAG, "onSaveData(), update called")
                    interactor.updateFlowerbed(Flowerbed(flowerbedId, name, description, comment))
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    {},
                    { error -> _errorsLiveData.value = error }
                )
        )
    }

    /**
     * Сохранение данных клумбы в лайвдату на время пересоздания экрана
     * @param flowerbedId идентификатор клумбы. Если в режиме вставке, то null
     * @param name имя клумба
     * @param description описание клумбы
     * @param comment комментарий к клумбе
     */
    fun onStoreData(flowerbedId: Long?, name: String, description: String, comment: String?) {
        _flowerbedLiveData.value = Flowerbed(
            flowerbedId = flowerbedId,
            name = name,
            description = description,
            comment = comment
        )
    }
}

private const val TAG = "${APP_TAG}.FlowerbedViewModel"