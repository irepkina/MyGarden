package com.irepka3.mygarden.ui.mainpage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.irepka3.mygarden.ui.flowerbed.list.FlowerbedListFragment
import com.irepka3.mygarden.ui.work.list.WorkListFragment

/**
 * Адаптер для вьюпейджера главной страницы
 *
 * Created by i.repkina on 10.11.2021.
 */
class MainPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private var pageCount: Int = 2

    override fun getItemCount(): Int {
        return pageCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FlowerbedListFragment()
            }
            1 -> {
                WorkListFragment()
            }
            else -> throw IllegalStateException("Invalid position = $position")
        }
    }
}