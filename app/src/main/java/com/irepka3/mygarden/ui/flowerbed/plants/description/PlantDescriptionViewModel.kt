package com.irepka3.mygarden.ui.flowerbed.plants.description

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.util.Const.APP_TAG
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * View-модель для фрагмента карточки растения
 * @param flowerbedId идентификатор клумбы.
 * @param plantId идентификтор растения. Может быть null если запись еще не сохранена в базу
 * @param interactor интерактор доменного слоя для работы с растениями [PlantInteractor]
 *
 * Created by i.repkina on 05.11.2021..
 */
class PlantDescriptionViewModel(private val flowerbedId: Long, private val plantId: Long?, private val interactor: PlantInteractor): ViewModel() {
    /**
     * LiveData клумбы
     */
    val plantLiveData = MutableLiveData<Plant>()
    /**
     * LiveData для вывода ошибки
     */
    val errorsLiveData = MutableLiveData<Throwable>()
    /**
     * LiveData для отображения индикатора загрузки данных
     */
    val progressLiveData = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
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
        Log.d(TAG, "loadData() called plantId = $plantId")
        progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable {
                if (plantId == null) {
                    Log.d(TAG, "loadData() create new Plant")
                    Plant(flowerbedId = flowerbedId, plantId = null)
                } else {
                    Log.d(TAG, "loadData() called Plant by Id = $plantId")
                    interactor.getPlant(plantId)
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressLiveData.value = false }
                .subscribe(
                    { plant -> plantLiveData.value = plant },
                    { error ->  errorsLiveData.value = error }
                )
        )
    }

    /**
     * Обновление данных растения
     * @param flowerbedId идентификатор клумбы.
     * @param plantId идентификатор растения. Если в режиме вставке, то null
     * @param name имя растения
     * @param description описание растения
     * @param comment комментарий к растению
     * @param count количество посаженных экземпляров растений
     * @param plantDate дата посадки
     */
    fun onSaveData(
        flowerbedId: Long,
        plantId: Long?,
        name: String,
        description: String,
        comment: String?,
        count: Int,
        plantDate: String?
    ){
        Log.d(
            TAG,
            "onSaveData() called with: flowerbedId = $flowerbedId, plantId = $plantId, name = $name, description = $description, comment = $comment, count = $count, datePlant = $plantDate"
        )

        val date = try {
            if (!plantDate.isNullOrBlank()) dateFormat.parse(plantDate).time else null
        } catch (e: Exception) {
            errorsLiveData.value = e
            return
        }

        progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                if (plantId == null) {
                    Log.d(TAG, "onSaveData(), insert called")
                    val plant = Plant(flowerbedId = flowerbedId, plantId = null,  name, description, comment, count, date)
                    val newPlantId = interactor.insertPlant(plant)
                    plantLiveData.postValue(plant.copy(plantId = newPlantId))
                } else {
                    Log.d(TAG, "onSaveData(), update called")
                    interactor.updatePlant(Plant(flowerbedId = flowerbedId, plantId = plantId, name, description, comment, count, date))
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressLiveData.value = false }
                .subscribe(
                    { },
                    { error -> errorsLiveData.value = error }
                )
        )
    }

    /**
     * Сохранение данных растения при закрытии фрагмента, если он создан
     * @param flowerbedId идентификатор клумбы.
     * @param plantId идентификатор растения. Если в режиме вставке, то null
     * @param name имя растения
     * @param description описание растения
     * @param comment комментарий к растению
     * @param count количество экземлпяров растения
     * @param plantDate дата посадки растения
     */
    fun onClose(
        flowerbedId: Long,
        plantId: Long?,
        name: String,
        description: String,
        comment: String?,
        count: Int,
        plantDate: String?
    ){
        if (plantId != null) {
            onSaveData(flowerbedId = flowerbedId, plantId = plantId, name, description, comment, count, plantDate)
        }
    }
}

private const val TAG = "${APP_TAG}.PlantDescriptionViewModel"