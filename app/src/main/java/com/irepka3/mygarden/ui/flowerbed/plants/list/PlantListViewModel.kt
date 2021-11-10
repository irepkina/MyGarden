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
 * @param flowerbedId идентификатор клумбы
 * @param interactor доменного слоя для работы с ратениями [PlantInteractor]
 *
 * Created by i.repkina on 01.11.2021.
 */
class PlantListViewModel(private val flowerbedId: Long, private val interactor: PlantInteractor): ViewModel() {
    /**
     * LiveData списка растений
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
     * Загрузка данных во view-модель
     */
    fun loadData(){
        progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable { interactor.getPlantsByFlowerbed(flowerbedId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressLiveData.value = false }
                .subscribe(
                    { list -> plantListLiveData.value = list },
                    { error ->  errorsLiveData.value = error }
                )
        )
    }


    /**
     * Удаление выбранной клумбы
     * @param plant выбранная клумба [Plant]
     */
    fun onDelete(plant: Plant){
        progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                interactor.deletePlant(plant)
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