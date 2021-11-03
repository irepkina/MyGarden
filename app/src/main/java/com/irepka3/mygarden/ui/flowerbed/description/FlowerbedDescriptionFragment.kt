package com.irepka3.mygarden.ui.flowerbed.description

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
import com.irepka3.mygarden.databinding.FragmentFlowerbedDescriptionBinding
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.factory.FlowerbedFactory
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Фрагмент для отображения описания клумбы
 *
 * Created by i.repkina on 31.10.2021.
 */
class FlowerbedDescriptionFragment(): Fragment() {
    private lateinit var binding: FragmentFlowerbedDescriptionBinding
    private var flowerbedId: Long? = null

    private val viewModel by viewModels<FlowerbedViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val flowerbedFactory = FlowerbedFactory(requireContext())
                return FlowerbedViewModel(flowerbedId, flowerbedFactory.getFlowerbedInteractor()) as T
            }
        }
    }

    companion object {
        /**
         * Создает фрагмент для редактирования описания клумбы
         * @param flowerbedId идентификатор клумбы         *
         */
        fun newInstanceUpdate(flowerbedId: Long): FlowerbedDescriptionFragment {
            return FlowerbedDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putString(MODE, MODE_UPDATE)
                }
            }
        }

        /**
         * Создает фрагмент для добавления описания новой клумбы
         */
        fun newInstanceInsert(): FlowerbedDescriptionFragment {
            return FlowerbedDescriptionFragment().apply {
                arguments = Bundle().apply { putString(MODE,  MODE_INSERT) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFlowerbedDescriptionBinding.inflate(inflater)
        readArguments()
        Log.d(TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState"
        )

        // подписка на life-data view-модели
        viewModel.flowerbedLiveData.observe(viewLifecycleOwner) { flowerbed -> showFlowerbed(flowerbed) }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result -> binding.progressBar.isVisible = result }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(this.context, error.message , Toast.LENGTH_SHORT).show()
        }
        viewModel.commandLiveData.observe(viewLifecycleOwner) {command ->
            when (command) {
                FlowerbedViewModel.Command.SHOW_FLOWERBED_LIST -> (this.activity as MainActivityIntf).showFlowerbedList()
                else -> Unit
            }
        }

        binding.saveButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener() called")
            viewModel.onSaveData(flowerbedId,
                binding.flowerbedName.text.toString(),
                binding.flowerbedDescription.text.toString(),
                binding.flowerbedComment.text.toString())
        }

        return binding.root
    }

    private fun readArguments(){
        Log.d(TAG, "readArguments() called")
        when (val mode = arguments?.getString(MODE)) {
            MODE_UPDATE -> {
                flowerbedId = arguments?.getLong(FLOWERBED_ID)
                if (flowerbedId == 0L)
                    throw Exception("Incorrect arguments: mode = $mode, flowerbedId = $flowerbedId" )
            }
            MODE_INSERT -> flowerbedId = null
            else -> throw Exception("Incorrect arguments: flowerbed mode: $mode" )
        }

        Log.d(TAG, "readArguments() success, flowerbedId = $flowerbedId")
    }

    private fun showFlowerbed(flowerbed: Flowerbed?){
        binding.flowerbedName.setText(flowerbed?.name)
        binding.flowerbedDescription.setText(flowerbed?.description)
        binding.flowerbedComment.setText(flowerbed?.comment)
    }
}

private const val TAG = "${APP_TAG}.FlowerbedFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"