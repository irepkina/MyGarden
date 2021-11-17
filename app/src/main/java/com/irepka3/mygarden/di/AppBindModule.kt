package com.irepka3.mygarden.di

import com.irepka3.mygarden.data.repository.DirRepositoryImpl
import com.irepka3.mygarden.data.repository.FlowerbedPhotoRepositoryImpl
import com.irepka3.mygarden.data.repository.FlowerbedRepositoryImpl
import com.irepka3.mygarden.data.repository.PlantPhotoRepositoryImpl
import com.irepka3.mygarden.data.repository.PlantRepositoryImpl
import com.irepka3.mygarden.data.repository.RepeatWorkRepositoryImpl
import com.irepka3.mygarden.data.repository.WorkRepositoryImpl
import com.irepka3.mygarden.domain.interactor.FileInteractor
import com.irepka3.mygarden.domain.interactor.FileInteractotImpl
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedInteractorImpl
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractor
import com.irepka3.mygarden.domain.interactor.FlowerbedPhotoInteractorImpl
import com.irepka3.mygarden.domain.interactor.PlantInteractor
import com.irepka3.mygarden.domain.interactor.PlantInteractorImpl
import com.irepka3.mygarden.domain.interactor.PlantPhotoInteractor
import com.irepka3.mygarden.domain.interactor.PlantPhotoInteractorImpl
import com.irepka3.mygarden.domain.interactor.RepeatWorkInteractor
import com.irepka3.mygarden.domain.interactor.RepeatWorkInteractorImpl
import com.irepka3.mygarden.domain.interactor.WorkInteractor
import com.irepka3.mygarden.domain.interactor.WorkInteractorImpl
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractor
import com.irepka3.mygarden.domain.interactor.WorkManagerInteractorImpl
import com.irepka3.mygarden.domain.repository.DirRepository
import com.irepka3.mygarden.domain.repository.FlowerbedPhotoRepository
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.domain.repository.PlantPhotoRepository
import com.irepka3.mygarden.domain.repository.PlantRepository
import com.irepka3.mygarden.domain.repository.RepeatWorkRepository
import com.irepka3.mygarden.domain.repository.WorkRepository
import dagger.Binds
import dagger.Module

/**
 * Dagger-модуль, возвращающий зависимости через механизм bind
 * Не содержит реализации, работает через связку binds-inject
 *
 * Created by i.repkina on 07.11.2021.
 */
@Module
interface AppBindModule {
    @Binds
    fun bindFlowerbedRepository(repository: FlowerbedRepositoryImpl): FlowerbedRepository

    @Binds
    fun bindPlantRepository(repository: PlantRepositoryImpl): PlantRepository

    @Binds
    fun bindFlowerbedPhotoRepository(repository: FlowerbedPhotoRepositoryImpl): FlowerbedPhotoRepository

    @Binds
    fun bindPlantPhotoRepository(repository: PlantPhotoRepositoryImpl): PlantPhotoRepository

    @Binds
    fun bindDirRepository(repository: DirRepositoryImpl): DirRepository

    @Binds
    fun bindFlowerbedInteractor(interactor: FlowerbedInteractorImpl): FlowerbedInteractor

    @Binds
    fun bindFlowerbedPhotoInteractor(interactor: FlowerbedPhotoInteractorImpl): FlowerbedPhotoInteractor

    @Binds
    fun bindPlantPhotoInteractor(interactor: PlantPhotoInteractorImpl): PlantPhotoInteractor

    @Binds
    fun bindPlantInteractor(interactor: PlantInteractorImpl): PlantInteractor

    @Binds
    fun bindFileInteractor(interactor: FileInteractotImpl): FileInteractor

    @Binds
    fun bindWorkRepository(repository: WorkRepositoryImpl): WorkRepository

    @Binds
    fun bindRepeatWorkRepository(repository: RepeatWorkRepositoryImpl): RepeatWorkRepository

    @Binds
    fun bindWorkInteractor(interactor: WorkInteractorImpl): WorkInteractor


    @Binds
    fun bindWorkManagerInteractor(interactor: WorkManagerInteractorImpl): WorkManagerInteractor

    @Binds
    fun bindRepeatWorkInteractor(interactor: RepeatWorkInteractorImpl): RepeatWorkInteractor
}