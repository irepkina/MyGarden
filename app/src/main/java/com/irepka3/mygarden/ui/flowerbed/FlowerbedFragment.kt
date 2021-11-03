package com.irepka3.mygarden.ui.flowerbed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.MarginPageTransformer
import com.irepka3.mygarden.R
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.irepka3.mygarden.databinding.FragmentFlowerbedPageBinding
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Фрагмент для отображения информации о клумбе
 */
class FlowerbedFragment : Fragment() {
     private lateinit var binding: FragmentFlowerbedPageBinding
     private lateinit var myViewPager2: ViewPager2
     private lateinit var myAdapter: FlowerbedAdapter
     private var flowerbedId: Long? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG,"onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        binding = FragmentFlowerbedPageBinding.inflate(inflater)
        readargument()

        myViewPager2 = binding.pager
        myAdapter = FlowerbedAdapter(flowerbedId, requireActivity().supportFragmentManager, requireActivity().lifecycle)
        myViewPager2.orientation  = ViewPager2.ORIENTATION_HORIZONTAL
        myViewPager2.adapter = myAdapter
        myViewPager2.setPageTransformer( MarginPageTransformer(MARGIN))

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_flowerbed_description_text)
                1 -> tab.text =  getString(R.string.tab_flowerbed_plants_text)
            }
        }.attach()

        return binding.root
    }

    companion object {
        /**
         * Создает фрагмент для редактирования клумбы
         * @param flowerbedId идентификатор клумбы         *
         */
        fun newInstanceUpdate(flowerbedId: Long): FlowerbedFragment {
            return FlowerbedFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putString(MODE, MODE_UPDATE)
                }
            }
        }

        /**
         * Создает фрагмент для добавления новой клумбы
         */
        fun newInstanceInsert(): FlowerbedFragment {
            return FlowerbedFragment().apply {
                arguments = Bundle().apply { putString(MODE,  MODE_INSERT) }
            }
        }
    }

    private fun readargument() {
        Log.d(TAG, "readArguments() called")
        when (val mode = arguments?.getString(MODE)) {
            MODE_UPDATE -> {
                flowerbedId = arguments?.getLong(FLOWERBED_ID)
                if (flowerbedId == 0L)
                    throw Exception("Incorrect arguments: mode = $mode, flowerbedId = $flowerbedId")
            }
            MODE_INSERT -> flowerbedId = null
            else -> throw Exception("Incorrect arguments: flowerbed mode: $mode")
        }

        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId")
    }
}

private const val TAG = "${APP_TAG}.FlowerbedPageFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"
private const val MARGIN = 1500