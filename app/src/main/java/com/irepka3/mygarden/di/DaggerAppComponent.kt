package com.irepka3.mygarden.di

import android.content.Context
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.interactor.PlantPhotoInteractor
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.domain.repository.PlantPhotoRepository
import com.irepka3.mygarden.domain.repository.PlantRepository
import dagger.Component

/**
 * Dagger-компонент приложения
 *
 * Created by i.repkina on 07.11.2021.
 */
@Component(modules = [AppProvideModule::class, AppBindModule::class])
interface DaggerAppComponent {
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

    // Возвращает интерактор клумбы
    fun getFlowerbedInteractor(): FlowerbedInteractor

    // Возвращает интерактор фотографий клумбы
    fun getFlowerbedPhotoInteractor(): FlowerbedPhotoInteractor

    // Возвращает интерактор фотографий растения
    fun getPlantPhotoInteractor(): PlantPhotoInteractor

    // Возвращает интерактор растений
    fun getPlantInteractor(): PlantInteractor

    // Возвращает интерактор
    fun getFileInteractor(): FileInteractor
}
