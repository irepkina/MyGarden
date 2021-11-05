package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.FragmentFlowerbedPhotoListBinding
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.factory.FlowerbedFactory
import com.irepka3.mygarden.ui.ItemTouchHelperFactory
import com.irepka3.mygarden.util.Const.APP_TAG
import com.irepka3.mygarden.util.recycleView.GridSpacingItemDecorator


/**
 * Фрагмент для отображения списка фотографий клумбы
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoListFragment: Fragment(), FlowerbedPhotoListAdapter.FlowerbedPhotoListAdapterCallback {
    private lateinit var binding: FragmentFlowerbedPhotoListBinding
    private val adapter = FlowerbedPhotoListAdapter(this)
    private var flowerbedId: Long = 0L

    private val viewModel by viewModels<FlowerbedPhotoListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val flowerbedFactory = FlowerbedFactory(requireContext())
                return FlowerbedPhotoListViewModel(
                    flowerbedId,
                    flowerbedFactory.getPhotoInteractor(),
                    flowerbedFactory.getFlowerbedPhotoInteractor()
                ) as T
            }
        }
    }


    companion object {
        /**
         * Создает фрагмент со списком фотографий клумбы
         * @param flowerbedId идентификатор клумбы         *
         */
        fun newInstance(flowerbedId: Long): FlowerbedPhotoListFragment {
            return FlowerbedPhotoListFragment().apply {
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
        binding = FragmentFlowerbedPhotoListBinding.inflate(inflater)

        readArguments()

        // подписка на life-data view-модели
        viewModel.flowerbedPhotoListLiveData.observe(viewLifecycleOwner) { flowerbedPhotoList -> adapter.updateItems(flowerbedPhotoList) }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result -> binding.progressBar.isVisible = result }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.d(TAG, "onCreateView() called with: error = ${error.message}")
            Toast.makeText(this.context, error.message , Toast.LENGTH_SHORT).show()
        }

        val itemDecorator = GridSpacingItemDecorator(
            requireContext().resources,
            COLUMN_COUNT,
            R.dimen.grid_space,
            true
        )

        binding.flowerbedPhotoRecyclerView.addItemDecoration(itemDecorator)

        this.binding.flowerbedPhotoRecyclerView.layoutManager = GridLayoutManager(context, COLUMN_COUNT)
        binding.flowerbedPhotoRecyclerView.adapter = adapter

        binding.floatingButtonFlowerbedPhoto.setOnClickListener {
            filesChooserContract.launch("image/*")
        }

        return binding.root
    }

    // Создание контракта для выбора фото
    private val filesChooserContract = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
        for (uri in uriList) {
            Log.d(TAG, "onActivityResult: uri = ${uri}")
            insertFlowerbedPhoto(uri)
        }
    }

    // Чтение аргументов
    private fun readArguments(){
        Log.d(TAG, "readArguments() called, id = $id")
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw Exception("Incorrect arguments: flowerbedId = $flowerbedId")
        Log.d(TAG, "readArguments() success, id = $id")
    }

    // Удаляет фотографию клумбы
    private fun deleteFlowerbedPhoto(flowerbedPhoto: FlowerbedPhoto) {
        viewModel.onDelete(flowerbedPhoto)
    }


     // Добавляет фотографию клумбы
    private fun insertFlowerbedPhoto(externalUri: Uri) {
        viewModel.onInsert(externalUri)
    }

    override fun onFlowerbedPhotoClick(flowerbedPhotoId: Long?) {
        TODO("Not yet implemented")
    }
}

private const val TAG = "${APP_TAG}.FlowerbedPhotoListFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val FLOWERBED_PHOTO_ID = "flowerbedPhotoId"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"
// количество столбцов в recycleview
private const val COLUMN_COUNT = 3
