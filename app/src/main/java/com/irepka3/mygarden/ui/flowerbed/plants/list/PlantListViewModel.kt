package com.irepka3.mygarden.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.model.Plant
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * View-модель для фрагмента списка растений
 * @param interactor доменного слоя для работы с клумбами [PlantInteractor]
 *
 * Created by i.repkina on 01.11.2021.
 */
class PlantListViewModel(private val flowerbedId: Long, private val interactor: PlantInteractor): ViewModel() {
    /**
     * LiveData списка клумб
     */
    val plantListLiveData = MutableLiveData<List<Plant>>()
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
            Single.fromCallable { interactor.getPlantsByFlowerbed(flowerbedId) }
                .subscribeOn(Schedulers.io())
                .doFinally { progressLiveData.postValue(false) }
                .subscribe(
                    { list -> plantListLiveData.postValue(list) },
                    { error ->  errorsLiveData.postValue(error) }
                )
        )
    }


    /**
     * Удаление выбранной клумбы
     * @param plant выбранная клумба [Plant]
     */
    fun onDelete(plant: Plant){
        progressLiveData.postValue(true)
        compositeDisposable.add(
            Completable.fromCallable {
                interactor.deletePlant(plant)
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