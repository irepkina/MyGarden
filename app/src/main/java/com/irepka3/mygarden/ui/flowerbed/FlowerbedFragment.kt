package com.irepka3.mygarden.ui.flowerbed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.irepka3.mygarden.R
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.databinding.FragmentFlowerbedPageBinding
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Фрагмент для отображения информации о клумбе
 */
class FlowerbedFragment : Fragment(), FlowerbedFragmentIntf {
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
        readArgument()

        myViewPager2 = binding.pager
        myAdapter = FlowerbedAdapter(this.childFragmentManager, this.lifecycle)
        myAdapter.setFlowerbedId(flowerbedId)
        myViewPager2.orientation  = ViewPager2.ORIENTATION_HORIZONTAL
        myViewPager2.adapter = myAdapter
        myViewPager2.setPageTransformer(MarginPageTransformer(MARGIN))

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_flowerbed_description_text)
                1 -> tab.text = getString(R.string.tab_flowerbed_plants_text)
                2 -> tab.text = getString(R.string.tab_flowerbed_photo_text)
            }
        }.attach()

        initToolBar()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        currentFlowerbedName = ""
    }

    private fun initToolBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            val actionBar = supportActionBar
            actionBar?.title = currentFlowerbedName

            actionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
            actionBar?.setDisplayShowHomeEnabled(supportFragmentManager.backStackEntryCount > 0)

            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun readArgument() {
        Log.d(TAG, "readArguments() called")
        when (val mode = arguments?.getString(MODE)) {
            MODE_UPDATE -> {
                flowerbedId = arguments?.getLong(FLOWERBED_ID)
                if (flowerbedId == 0L)
                    throw IllegalStateException("Incorrect arguments: mode = $mode, flowerbedId = $flowerbedId")
            }
            MODE_INSERT -> flowerbedId = null
            else -> throw IllegalStateException("Incorrect arguments: flowerbed mode: $mode")
        }
        currentFlowerbedName = arguments?.getString(CAPTION) ?: ""

        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId")
    }

    override fun updateFlowerbedId(flowerbedId: Long) {
        myAdapter.setFlowerbedId(flowerbedId)
    }

    override fun updateCaption(caption: String) {
        currentFlowerbedName = caption
        binding.collapsingToolbar.title = currentFlowerbedName
    }

    companion object {
        var currentFlowerbedName: String = ""

        /**
         * Создает фрагмент для редактирования клумбы
         * @param flowerbedId идентификатор клумбы
         * @param flowerbedName название клумбы
         */
        fun newInstanceUpdate(flowerbedId: Long, flowerbedName: String): FlowerbedFragment {
            return FlowerbedFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putString(MODE, MODE_UPDATE)
                    putString(CAPTION, flowerbedName)
                }
            }
        }

        /**
         * Создает фрагмент для добавления новой клумбы
         */
        fun newInstanceInsert(): FlowerbedFragment {
            return FlowerbedFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_INSERT)
                    putString(
                        CAPTION,
                        dagger().getContext().getString(R.string.new_flowerbed_caption)
                    )
                }
            }
        }
    }
}

/**
 * Интрефейс фрагмента вьюпейджера клумбы
 */
interface FlowerbedFragmentIntf {
    fun updateFlowerbedId(flowerbedId: Long)
    fun updateCaption(caption: String)
}

private const val TAG = "${APP_TAG}.FlowerbedFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val CAPTION = "caption"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"
private const val MARGIN = 1500