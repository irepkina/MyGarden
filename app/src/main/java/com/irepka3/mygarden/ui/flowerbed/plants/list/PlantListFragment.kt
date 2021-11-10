package com.irepka3.mygarden.ui.flowerbed.plants.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.databinding.FragmentPlantlistBinding
import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.ui.ItemTouchHelperFactory
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.ui.list.PlantListViewModel

/**
 * Фрагмент для отображения списка растений
 *
 * Created by i.repkina on 31.10.2021.
 */
class PlantListFragment: Fragment(), PlantListAdapter.PlantListAdapterCallback {
    private lateinit var binding: FragmentPlantlistBinding
    private val adapter = PlantListAdapter(this)
    private var flowerbedId: Long = 0L

    private val viewModel by viewModels<PlantListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PlantListViewModel(flowerbedId, dagger().getPlantInteractor()) as T
            }
        }
    }

    companion object {
        /**
         * Создает фрагмент со списком растений клумбы
         * @param flowerbedId идентификатор клумбы         *
         */
        fun newInstance(flowerbedId: Long): PlantListFragment {
            return PlantListFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantlistBinding.inflate(inflater)

        readArguments()

        // подписка на life-data view-модели
        viewModel.plantListLiveData.observe(viewLifecycleOwner) { plantList -> adapter.updateItems(plantList) }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result -> binding.progressBar.isVisible = result }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.d(TAG, "onCreateView() called with: error = ${error.message}")
            Toast.makeText(this.context, error.message , Toast.LENGTH_SHORT).show()
        }


        viewModel.loadData()

        val itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.recycleview_divider_transparent, this.activity?.theme)
        itemDecorator.setDrawable(drawable)
        binding.plantRecyclerView.addItemDecoration(itemDecorator)

        this.binding.plantRecyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        binding.plantRecyclerView.adapter = adapter

        binding.floatingButtonPlant.setOnClickListener { (requireActivity() as MainActivityIntf).showPlantFragment(flowerbedId = flowerbedId, plantId = null) }

        val itemTouchHelper = ItemTouchHelperFactory.getItemTouchHelper { adapterPosition ->
            deletePlant(adapter.getItem(adapterPosition))
        }
        itemTouchHelper.attachToRecyclerView(binding.plantRecyclerView)

        return binding.root
    }

    private fun readArguments(){
        Log.d(TAG, "readArguments() called, id = $id")
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw Exception("Incorrect arguments: flowerbedId = $flowerbedId")
        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId")

    }

    override fun onPlantClick(flowerbedId:Long, plantId: Long?) {
        Log.d(TAG, "onPlantClick() called with: id = $plantId")
        (requireActivity() as MainActivityIntf).showPlantFragment(flowerbedId = flowerbedId, plantId = plantId)
    }

    /**
     * Удаляет растение
     */
   private fun deletePlant(plant: Plant) {
        viewModel.onDelete(plant)
    }
}

private const val TAG = "PlantsListFragment"
private const val FLOWERBED_ID = "flowerbedId"