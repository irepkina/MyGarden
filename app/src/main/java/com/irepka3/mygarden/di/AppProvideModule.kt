package com.irepka3.mygarden.di

import android.app.Application
import android.content.Context
import com.irepka3.mygarden.data.database.AppRoomDataBase
import dagger.Module
import dagger.Provides

/**
 * Dagger модуль, создающий заивисимости, требующие контекста
 * Добавляем provide-функции с реализацией создания объектов
 *
 * Created by i.repkina on 07.11.2021.
 */
@Module
class AppProvideModule(private val application: Application) {
    @Provides
    fun provideAppContext(): Context = application.applicationContext

    @Provides
    fun provideDataBase(context: Context): AppRoomDataBase {
        return AppRoomDataBase.getInstance(context)
    }
}