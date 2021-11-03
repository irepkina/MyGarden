package com.irepka3.mygarden.ui.flowerbed

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.irepka3.mygarden.ui.flowerbed.description.FlowerbedDescriptionFragment
import com.irepka3.mygarden.ui.flowerbed.plants.list.PlantListFragment
import com.irepka3.mygarden.util.Const
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Адаптер для вьюпейджера клумбы
 * @param flowerbedId идентификатор клумбы
 * @param fragmentManager менеджер фрагментов главной активити [FragmentManager]
 * @param lifecycle жизненный цикл главной активити [Lifecycle]
 *
 * Created by i.repkina on 02.11.2021.
 */
class FlowerbedAdapter(
    private val flowerbedId: Long?,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment() called with: position = $position")
        return when (position) {
            0 -> {
                if (flowerbedId != null)
                    FlowerbedDescriptionFragment.newInstanceUpdate(flowerbedId)
                else
                    FlowerbedDescriptionFragment.newInstanceInsert()
            }
            1 ->  {
                if (flowerbedId == null) throw Exception("FlowerBedId in plant can't be null")
                PlantListFragment.newInstance(flowerbedId)
            }
            else -> Fragment()
        }
    }
}

private const val TAG ="${APP_TAG}.FlowerbedAdapter"
private const val PAGE_COUNT = 2