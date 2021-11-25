package com.irepka3.mygarden.common.rules

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.ExternalResource


/**
 * Класс, формирующий правило подмены диспетчера потоков в rx-е на тестовый диспетчер потоков
 *
 * Created by i.repkina on 21.11.2021.
 */
class RxSchedulersRule : ExternalResource() {


    /**
     * подменяем в начале теста диспетчер потоков на тестовый
     */
    override fun before() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    /**
     * после заверещения теста сбрасываем подмененный диспетчер потоков
     */
    override fun after() {
        RxJavaPlugins.reset()
    }

}