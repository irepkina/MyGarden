package com.irepka3.mygarden.ui.flowerbed.plants

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.irepka3.mygarden.ui.flowerbed.plants.description.PlantDescriptionFragment
import com.irepka3.mygarden.ui.flowerbed.plants.photo.list.PlantPhotoListFragment
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Адаптер для вьюпейджера растения
 * @param flowerbedId идентификатор клумбы
 * @param fragmentManager менеджер фрагментов главной активити [FragmentManager]
 * @param lifecycle жизненный цикл главной активити [Lifecycle]
 *
 * Created by i.repkina on 06.11.2021.
 */
class PlantAdapter(
    private val flowerbedId: Long,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    private var pageCount: Int = 1
    private var plantId: Long? = null

    override fun getItemCount(): Int {
        return pageCount
    }

    fun setPlantId(plantId: Long?) {
        Log.d(TAG, "setPlantId() called with: plantId = $plantId, this.plantId = ${this.plantId}")
        if (this.plantId != plantId) {
            this.plantId = plantId
            pageCount = if (plantId != null) PAGE_COUNT else 1
            notifyDataSetChanged()
        }
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment() called with: position = $position")
        val flowerbedId = this.flowerbedId
        val plantId = this.plantId
        return when (position) {
            0 -> {
                if (plantId != null)
                    PlantDescriptionFragment.newInstanceUpdate(
                        flowerbedId = flowerbedId,
                        plantId = plantId
                    )
                else
                    PlantDescriptionFragment.newInstanceInsert(flowerbedId)
            }
            1 -> {
                if (plantId == null) throw IllegalStateException("PlantId in plant can't be null")
                PlantPhotoListFragment.newInstance(flowerbedId = flowerbedId, plantId = plantId)
            }
            else -> throw IllegalStateException("Invalid position = $position")
        }
    }
}

private const val TAG = "${APP_TAG}.PlantAdapter"
private const val PAGE_COUNT = 2