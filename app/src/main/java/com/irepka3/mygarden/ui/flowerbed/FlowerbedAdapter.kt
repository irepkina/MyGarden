package com.irepka3.mygarden.ui.flowerbed

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.irepka3.mygarden.ui.flowerbed.description.FlowerbedDescriptionFragment
import com.irepka3.mygarden.ui.flowerbed.photo.list.FlowerbedPhotoListFragment
import com.irepka3.mygarden.ui.flowerbed.plants.list.PlantListFragment
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Адаптер для вьюпейджера клумбы
 * @param fragmentManager менеджер фрагментов главной активити [FragmentManager]
 * @param lifecycle жизненный цикл главной активити [Lifecycle]
 *
 * Created by i.repkina on 02.11.2021.
 */
class FlowerbedAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    private var pageCount: Int = 1
    private var flowerbedId: Long? = null

    override fun getItemCount(): Int {
        return pageCount
    }

    fun setFlowerbedId(flowerbedId: Long?){
        if (this.flowerbedId != flowerbedId) {
            this.flowerbedId = flowerbedId
            pageCount = if (flowerbedId != null) PAGE_COUNT else 1
            notifyDataSetChanged()
        }
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment() called with: position = $position")
        val flowerbedId = this.flowerbedId
        return when (position) {
            0 -> {
                if (flowerbedId != null)
                    FlowerbedDescriptionFragment.newInstanceUpdate(flowerbedId)
                else
                    FlowerbedDescriptionFragment.newInstanceInsert()
            }
            1 ->  {
                if (flowerbedId == null) throw IllegalStateException("FlowerBedId in plant can't be null")
                PlantListFragment.newInstance(flowerbedId)
            }
            2 -> {
                if (flowerbedId == null) throw IllegalStateException("FlowerBedId in photo can't be null")
                FlowerbedPhotoListFragment.newInstance(flowerbedId)
            }
            else -> throw IllegalStateException("Invalid position = $position")
        }
    }
}

private const val TAG ="${APP_TAG}.FlowerbedAdapter"
private const val PAGE_COUNT = 3