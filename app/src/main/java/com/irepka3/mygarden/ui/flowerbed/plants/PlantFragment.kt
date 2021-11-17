package com.irepka3.mygarden.ui.flowerbed.plants

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.FragmentPlantPageBinding
import com.irepka3.mygarden.util.Const

/**
 * Фрагмент для отображения информации о растении
 *
 * Created by i.repkina on 06.11.2021.
 */
class PlantFragment: Fragment(), PlantFragmentIntf {
    private lateinit var binding: FragmentPlantPageBinding
    private lateinit var myViewPager2: ViewPager2
    private lateinit var myAdapter: PlantAdapter
    private var flowerbedId: Long = 0L
    private var plantId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG,"onCreateView() called")
        binding = FragmentPlantPageBinding.inflate(inflater)
        readArgument()

        myViewPager2 = binding.pager
        myAdapter = PlantAdapter(flowerbedId, this.childFragmentManager, this.lifecycle)
        myAdapter.setPlantId(plantId)
        myViewPager2.orientation  = ViewPager2.ORIENTATION_HORIZONTAL
        myViewPager2.adapter = myAdapter
        myViewPager2.setPageTransformer(MarginPageTransformer(MARGIN))

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_plant_description_text)
                1 -> tab.text = getString(R.string.tab_plant_photo_text)
            }
        }.attach()

        return binding.root
    }

    private fun readArgument() {
        Log.d(TAG, "readArguments() called")
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw IllegalStateException("Incorrect arguments: flowerbedId = $flowerbedId")

        when (val mode = arguments?.getString(MODE)) {
            MODE_UPDATE -> {
                plantId = arguments?.getLong(PLANT_ID)
                if (plantId == 0L)
                    throw IllegalStateException("Incorrect arguments: mode = $mode, plantId = $plantId")
            }
            MODE_INSERT -> plantId = null
            else -> throw IllegalStateException("Incorrect arguments: flowerbed mode: $mode")
        }

        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId, plantId = $plantId")
    }

    override fun updatePlantId(plantId: Long) {
        Log.d(TAG, "updatePlantId() called with: plantId = $plantId")
        myAdapter.setPlantId(plantId)
    }

    companion object {
        /**
         * Создает фрагмент для редактирования растения
         * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun newInstanceUpdate(flowerbedId: Long, plantId: Long): PlantFragment {
            Log.d(
                TAG,
                "newInstanceUpdate() called with: flowerbedId = $flowerbedId, plantId = $plantId"
            )
            return PlantFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putLong(PLANT_ID, plantId)
                    putString(MODE, MODE_UPDATE)
                }
            }
        }

        /**
         * Создает фрагмент для добавления нового растения
         */
        fun newInstanceInsert(flowerbedId: Long): PlantFragment {
            return PlantFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putString(MODE, MODE_INSERT)
                }
            }
        }
    }
}

/**
 * Интрефейс фрагмента вьюпейджера растения
 */
interface PlantFragmentIntf {
    fun updatePlantId(plantId: Long)
}
private const val TAG = "${Const.APP_TAG}.PlantFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val PLANT_ID = "plantId"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"
private const val MARGIN = 1500