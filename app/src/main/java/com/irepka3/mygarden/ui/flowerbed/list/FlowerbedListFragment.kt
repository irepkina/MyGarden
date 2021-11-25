package com.irepka3.mygarden.ui.flowerbed.list

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
import com.irepka3.mygarden.databinding.FragmentFlowerbedlistBinding
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.ui.util.recycleView.ItemTouchHelperFactory
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Фрагмент для отображения списка клумб
 *
 * Created by i.repkina on 31.10.2021.
 */
class FlowerbedListFragment: Fragment(), FlowerbedListAdapter.FlowerbedAdapterCallback {
    private lateinit var binding: FragmentFlowerbedlistBinding
    private val adapter = FlowerbedListAdapter(this)

    private val viewModel by viewModels<FlowerbedListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FlowerbedListViewModel(
                    dagger().getFlowerbedInteractor()
                ) as T
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG,"onCreateView() called")
        binding = FragmentFlowerbedlistBinding.inflate(inflater)

        // подписка на life-data view-модели
        viewModel.flowerbedLiveData.observe(viewLifecycleOwner) { flowerbedList ->
            adapter.updateItems(flowerbedList)
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.e(TAG, "onCreateView() called with: error = ${error.message}", error)
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.loadData()

        val itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        val drawable = this.resources.getDrawable(
            R.drawable.recycleview_divider_transparent,
            this.activity?.theme
        )
        itemDecorator.setDrawable(drawable)
        binding.flowerbedRecyclerView.addItemDecoration(itemDecorator)

        binding.flowerbedRecyclerView.layoutManager = LinearLayoutManager(
            this.context,
            RecyclerView.VERTICAL,
            false
        )
        binding.flowerbedRecyclerView.adapter = adapter

        binding.floatingButtonAddFlowerbed.setOnClickListener {
            (requireActivity() as MainActivityIntf).showFlowerbedFragment(null, "")
        }

        val itemTouchHelper = ItemTouchHelperFactory.getItemTouchHelper { adapterPosition ->
            deleteFlowerbed(adapter.getItem(adapterPosition))
        }
        itemTouchHelper.attachToRecyclerView(binding.flowerbedRecyclerView)

        return binding.root
    }

    override fun onFlowerbedClick(flowerbedId: Long, flowerbedName: String) {
        Log.d(TAG, "onFlowerBedClick() called with: id = $flowerbedId")
        (this.activity as MainActivityIntf).showFlowerbedFragment(flowerbedId, flowerbedName)
    }

    /**
     * Удаляет клумбу
     */
    private fun deleteFlowerbed(flowerbed: Flowerbed) {
        viewModel.onDelete(flowerbed)
    }
}

private const val TAG = "${APP_TAG}.FlowerbedListFragment"