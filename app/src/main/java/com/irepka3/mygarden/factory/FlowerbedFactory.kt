package com.irepka3.mygarden.factory

import android.content.Context
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.repository.FlowerbedPhotoRepositoryImpl
import com.irepka3.mygarden.data.repository.FlowerbedRepositoryImpl
import com.irepka3.mygarden.data.repository.PlantRepositoryImpl
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractorImpl
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractorImpl
import com.irepka3.mygarden.domain.interactor.PhotoInteractor
import com.irepka3.mygarden.domain.interactor.PhotoInteractotImpl
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.interactor.PlantInteractorImpl
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.domain.repository.PlantRepository

/**
 * Фабрика, создающая экземпляры объектов доменного слоя
 *
 * Created by i.repkina on 01.11.2021.
 */
class FlowerbedFactory(context: Context) {
    private val context: Context = context.applicationContext

    // Создание репозитория клумб (доменный слой)
    fun getFlowerbedRepository(): FlowerbedRepository {
        return FlowerbedRepositoryImpl(getDataBase())
    }

    // Создание репозитория растений (доменный слой)
    fun getPlantRepository(): PlantRepository {
        return PlantRepositoryImpl(getDataBase())
    }

    // Создание репозитория фото клумбы (доменный слой)
    fun getFlowerbedPhotoRepository(): FlowerbedPhotoRepository {
        return FlowerbedPhotoRepositoryImpl(getDataBase())
    }

    // Создание интерактора клумбы
    fun getFlowerbedInteractor(): FlowerbedInteractor{
        return FlowerbedInteractorImpl(getFlowerbedRepository())
    }

    // Создание интерактора фотографий клумбы
    fun getFlowerbedPhotoInteractor(): FlowerbedPhotoInteractor {
        return FlowerbedPhotoInteractorImpl(getFlowerbedPhotoRepository())
    }

    // Создание интерактора растений
    fun getPlantInteractor(): PlantInteractor {
        return PlantInteractorImpl(getPlantRepository())
    }

    // Создание базы данных
    fun getDataBase(): AppRoomDataBase{
        return AppRoomDataBase.getInstance(context)
    }

    // Создание интерактора
    fun getPhotoInteractor(): PhotoInteractor{
        return PhotoInteractotImpl(context)
    }
}