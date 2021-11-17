package com.irepka3.mygarden.di

import android.content.Context
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.interactor.PlantPhotoInteractor
import com.irepka3.mygarden.domain.interactor.RepeatWorkInteractor
import com.irepka3.mygarden.domain.interactor.WorkInteractor
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractor
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.domain.repository.PlantPhotoRepository
import com.irepka3.mygarden.domain.repository.PlantRepository
import com.irepka3.mygarden.domain.repository.RepeatWorkRepository
import com.irepka3.mygarden.domain.repository.WorkRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger-компонент приложения
 *
 * Created by i.repkina on 07.11.2021.
 */
@Singleton
@Component(modules = [AppProvideModule::class, AppBindModule::class])
interface AppComponent {
    // Контекст приложения
    fun getContext(): Context

    // Возвращает базу данных
    fun getDataBase(): AppRoomDataBase

    // Возвращает репозиторий клумб (доменный слой)
    fun getFlowerbedRepository(): FlowerbedRepository

    // Возвращает репозиторий растений (доменный слой)
    fun getPlantRepository(): PlantRepository

    // Возвращает репозиторий фото клумбы (доменный слой)
    fun getFlowerbedPhotoRepository(): FlowerbedPhotoRepository

    // Возвращает репозиторий фото растений (доменный слой)
    fun getPlantPhotoRepository(): PlantPhotoRepository

    // Возвращает репозиторий с папками локального хранилища
    fun getDirRepository(): DirRepository

    // Возвращает репозиторий работы
    fun getWorkRepository(): WorkRepository

    // Возвращает репозиторий повторяющейся работы
    fun getRepeatWorkRepository(): RepeatWorkRepository

    // Возвращает интерактор клумбы
    fun getFlowerbedInteractor(): FlowerbedInteractor

    // Возвращает интерактор повторяющейся работы
    fun getRepeatWorkInteractor(): RepeatWorkInteractor

    // Возвращает интерактор работы
    fun getWorkInteractor(): WorkInteractor

    // Возвращает интерактор однократных и повторяющихся работ
    fun getWorkManagerInteractor(): WorkManagerInteractor

    // Возвращает интерактор фотографий клумбы
    fun getFlowerbedPhotoInteractor(): FlowerbedPhotoInteractor

    // Возвращает интерактор фотографий растения
    fun getPlantPhotoInteractor(): PlantPhotoInteractor

    // Возвращает интерактор растений
    fun getPlantInteractor(): PlantInteractor

    // Возвращает интерактор
    fun getFileInteractor(): FileInteractor
}
