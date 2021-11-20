package com.irepka3.mygarden

import android.app.Application
import com.irepka3.mygarden.di.AppComponent
import com.irepka3.mygarden.di.AppProvideModule
import com.irepka3.mygarden.di.DaggerAppComponent

/**
 * Created by i.repkina on 07.11.2021.
 */
class MyGardenApplication : Application() {

    companion object {
        lateinit var daggerAppComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        daggerAppComponent = initDagger()
    }

    private fun initDagger() = DaggerAppComponent.builder()
        .appProvideModule(AppProvideModule(this))
        .build()
}

fun dagger(): AppComponent = MyGardenApplication.daggerAppComponent