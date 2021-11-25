package com.irepka3.mygarden.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.ActivityMainBinding
import com.irepka3.mygarden.ui.flowerbed.FlowerbedFragment
import com.irepka3.mygarden.ui.flowerbed.photo.photo.FragmentFlowerbedPhoto
import com.irepka3.mygarden.ui.flowerbed.plants.PlantFragment
import com.irepka3.mygarden.ui.flowerbed.plants.photo.photo.FragmentPlantPhoto
import com.irepka3.mygarden.ui.mainpage.MainPageFragment
import com.irepka3.mygarden.ui.work.description.FragmentWorkManager
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.ui.work.model.WorkUIId
import com.irepka3.mygarden.ui.work.schedule.FragmentSchedule
import com.irepka3.mygarden.util.Const.APP_TAG

class MainActivity : AppCompatActivity(), MainActivityIntf {
    private lateinit var binding: ActivityMainBinding
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // открываем главный экран только если это запуск приложения
        // в остальных случаях андроид сам восстановит
        if (savedInstanceState == null) {
            showMainPage()
        }
    }

    override fun showMainPage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainPageFragment())
            .commit()
    }

    override fun showFlowerbedFragment(flowerbedId: Long?, flowerbedName: String?) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                if (flowerbedId !== null) FlowerbedFragment.newInstanceUpdate(
                    flowerbedId,
                    flowerbedName ?: ""
                ) else
                    FlowerbedFragment.newInstanceInsert()
            )
            .addToBackStack(null)
            .commit()
    }

    override fun showPlantFragment(flowerbedId: Long, plantId: Long?, plantName: String?) {
        this.supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                if (plantId !== null) PlantFragment.newInstanceUpdate(
                    flowerbedId = flowerbedId,
                    plantId = plantId,
                    plantName ?: ""
                ) else
                    PlantFragment.newInstanceInsert(flowerbedId)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun showFlowerbedPhoto(flowerbedId: Long, photoPosition: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FragmentFlowerbedPhoto.newInstance(flowerbedId, photoPosition))
            .addToBackStack(null)
            .commit()
    }

    override fun showPlantPhoto(flowerbedId: Long, plantId: Long, photoPosition: Int) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                FragmentPlantPhoto.newInstance(
                    flowerbedId = flowerbedId,
                    plantId = plantId,
                    photoPosition
                ),
                null
            )
            .addToBackStack(null)
            .commit()
    }

    override fun showWorkManagerFragment(workUIId: WorkUIId) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FragmentWorkManager.newInstance(workUIId))
            .addToBackStack(null)
            .commit()
    }

    override fun showScheduleFragment(scheduleData: ScheduleUIModel?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FragmentSchedule.newInstance(scheduleData))
            .addToBackStack(null)
            .commit()
    }
}

interface MainActivityIntf {
    /**
     * Показать главный экран
     */
    fun showMainPage()

    /**
     * Показать фрагмент с карточкой клумбы
     * @param flowerbedId идентификатор клумбы
     * @param flowerbedName название клумбы
     */
    fun showFlowerbedFragment(flowerbedId: Long?, flowerbedName: String?)

    /**
     * Показать фрагмент с растениями
     * @param flowerbedId идентификатор клумбы
     * @param plantId идентификатор растения
     * @param plantName название растения
     */
    fun showPlantFragment(flowerbedId: Long, plantId: Long?, plantName: String?)

    /**
     * Показать фрагмент фото клумбы
     * @param flowerbedId идентификатор клумбы
     * @param photoPosition номер фото
     */
    fun showFlowerbedPhoto(flowerbedId: Long, photoPosition: Int)

    /**
     * Показать фрагмент с фото растения
     * @param flowerbedId идентификатор клумбы
     * @param plantId идентификатор растения
     * @param photoPosition номер фото
     */
    fun showPlantPhoto(flowerbedId: Long, plantId: Long, photoPosition: Int)

    /**
     * Показать фрагмент для редактирования работы
     * @param workUIId - идентификаторы работы
     */
    fun showWorkManagerFragment(workUIId: WorkUIId)

    /**
     * Показать фрагмент для редактирования расписания
     * @param scheduleData настройки расписания [ScheduleUIModel]
     */
    fun showScheduleFragment(scheduleData: ScheduleUIModel?)

}

private const val  TAG = "$APP_TAG.MainActivity"