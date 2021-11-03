package com.irepka3.mygarden.factory

import android.content.Context
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.repository.FlowerbedRepositoryImpl
import com.irepka3.mygarden.data.repository.PlantRepositoryImpl
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractorImpl
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.interactor.PlantInteractorImpl
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

    // Создание интерактора клумбы
    fun getFlowerbedInteractor(): FlowerbedInteractor{
        return FlowerbedInteractorImpl(getFlowerbedRepository())
    }

    // Создание репозитория растений (доменный слой)
    fun getPlantRepository(): PlantRepository {
        return PlantRepositoryImpl(getDataBase())
    }

    // Создание интерактора растений
    fun getPlantInteractor(): PlantInteractor {
        return PlantInteractorImpl(getPlantRepository())
    }

    // Создание базы данных
    fun getDataBase(): AppRoomDataBase{
        return AppRoomDataBase.getInstance(context)
    }
}