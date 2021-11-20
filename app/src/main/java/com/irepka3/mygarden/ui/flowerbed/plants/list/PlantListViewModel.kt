package com.irepka3.mygarden.ui.list

import androidx.lifecycle.LiveData
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
class PlantListViewModel(
    private val flowerbedId: Long,
    private val interactor: PlantInteractor
) : ViewModel() {

    private val _plantListLiveData = MutableLiveData<List<Plant>>()

    /**
     * LiveData списка растений
     */
    val plantListLiveData: LiveData<List<Plant>> = _plantListLiveData

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
     * Взывает загрузку данных в событии создания вью
     */
    fun onCreateView() {
        loadData()
    }

    /**
     * Загрузка данных во view-модель
     */
    private fun loadData() {
        _progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable { interactor.getPlantsByFlowerbed(flowerbedId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    { list -> _plantListLiveData.value = list },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }


    /**
     * Удаление выбранной клумбы
     * @param plant выбранная клумба [Plant]
     */
    fun onDelete(plant: Plant){
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                interactor.deletePlant(plant)
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