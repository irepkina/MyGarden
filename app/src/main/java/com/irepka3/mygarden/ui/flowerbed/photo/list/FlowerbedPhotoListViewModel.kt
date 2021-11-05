package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.interactor.PhotoInteractor
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * View-модель для фрагмента списка фотографий клумбы
 * @param flowerbedId идентификатор клумбы
 * @param photoInteractor интерактор доменного слоя для работы с фотографиями [PhotoInteractor]
 * @param flowerbedPhotoInteractor интерактор доменного слоя для работы с фотографиями клумбы [FlowerbedPhotoInteractor]
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoListViewModel(private val flowerbedId: Long,
                                  private val photoInteractor: PhotoInteractor,
                                  private val flowerbedPhotoInteractor: FlowerbedPhotoInteractor): ViewModel() {
    /**
     * LiveData списка клумб
     */
    val flowerbedPhotoListLiveData = MutableLiveData<List<FlowerbedPhoto>>()
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
     * Загрузка данных во view-модель при создании
     */
    init {
        loadData()
    }

    /**
     * Загрузка данных во view-модель
     */
    private fun loadData(){
        progressLiveData.postValue(true)
        compositeDisposable.add(
            Single.fromCallable { flowerbedPhotoInteractor.getAllByFlowerbedId(flowerbedId) }
                .subscribeOn(Schedulers.io())
                .doFinally { progressLiveData.postValue(false) }
                .subscribe(
                    { list -> flowerbedPhotoListLiveData.postValue(list) },
                    { error ->  errorsLiveData.postValue(error) }
                )
        )
    }

    /**
     * Удаление выбранной фотографии клумбы
     * @param flowerbedPhoto выбранная фотография клумбы [FlowerbedPhoto]
     */
    fun onDelete(flowerbedPhoto: FlowerbedPhoto){
        progressLiveData.postValue(true)
        compositeDisposable.add(
            Completable.fromCallable {
                flowerbedPhotoInteractor.deleteFlowerbedPhoto(flowerbedPhoto)
            }
                .subscribeOn(Schedulers.io())
                .doFinally { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        loadData()
                    },
                    { error -> errorsLiveData.postValue(error) }
                )
        )
    }

    fun onInsert(externalUri: Uri){
        progressLiveData.postValue(true)
        compositeDisposable.add(
            Completable.fromCallable {
                val uri = photoInteractor.copyFileToLocalStorage(externalUri).toString()
                val flowerbedPhoto = FlowerbedPhoto(null, flowerbedId, uri)
                flowerbedPhotoInteractor.insertFlowerbedPhoto(flowerbedPhoto)
            }
                .subscribeOn(Schedulers.io())
                .doFinally { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        loadData()
                    },
                    { error -> errorsLiveData.postValue(error) }
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