package com.irepka3.mygarden

import android.app.Application
import com.irepka3.mygarden.di.DaggerAppComponent
import com.irepka3.mygarden.di.AppProvideModule
import com.irepka3.mygarden.di.DaggerDaggerAppComponent

/**
 * Created by i.repkina on 07.11.2021.
 */
class MyGardenApplication : Application() {

    companion object {
        lateinit var daggerAppComponent: DaggerAppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        daggerAppComponent = initDagger()
    }

    private fun initDagger() = DaggerDaggerAppComponent.builder()
        .appProvideModule(AppProvideModule(this))
        .build()
}

fun dagger(): DaggerAppComponent = MyGardenApplication.daggerAppComponent