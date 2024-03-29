package com.irepka3.mygarden.ui.photo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.ui.photo.model.Photo
import com.irepka3.mygarden.util.Const.APP_TAG
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Базовая View-модель для фрагмента со списком фотографий
 * @param fileInteractor интерактор доменного слоя для работы с фотографиями [FileInteractor]
 *
 * Created by i.repkina on 07.11.2021.
 */
abstract class BasePhotoViewModel(
    private val fileInteractor: FileInteractor
) : ViewModel() {

    private val _photoListLiveData = MutableLiveData<List<Photo>>()

    /**
     * LiveData списка фотографий
     */
    val photoListLiveData: LiveData<List<Photo>> = _photoListLiveData

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
     * Обновление данных во view-модель при создании вью
     */
    fun onCreateView() {
        loadData()
    }

    /**
     * Загрузка данных во view-модель
     */
    protected fun loadData() {
        _progressLiveData.value = true
        compositeDisposable.add(
            Single.fromCallable { doLoadData() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _progressLiveData.value = false }
                .subscribe(
                    { list -> _photoListLiveData.value = list },
                    { error -> _errorsLiveData.value = error }
                )
        )
    }

    /**
     * Удаление выбранной фотографии
     * @param photoList выбранные для удаления фотографии [List<Photo>]
     */
    fun onDelete(photoList: List<Photo>) {
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                doDelete(photoList)
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

    fun onInsert(externalUri: Uri) {
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                doInsert(
                    fileInteractor.copyFileToLocalStorage(getPhotoDir(), externalUri).toString()
                )
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
     * Установить фото по умолчанию
     * @param photoId идентификатор фото по умолчанию
     */
    fun onSelected(photoId: Long) {
        Log.d(TAG, "onSelected() called with: photoId = $photoId")
        _progressLiveData.value = true
        compositeDisposable.add(
            Completable.fromCallable {
                doSelected(photoId)
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

    protected abstract fun doLoadData(): List<Photo>
    protected abstract fun doInsert(uri: String)
    protected abstract fun doDelete(photoList: List<Photo>)
    protected abstract fun doSelected(photoId: Long)
    protected abstract fun getPhotoDir(): File

    /**
     * Очищаем подписки на rx при удалении вью-модели
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

private const val TAG = "{$APP_TAG}.BasePhotoViewModel"