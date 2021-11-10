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
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.databinding.FragmentFlowerbedDescriptionBinding
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.ui.flowerbed.FlowerbedFragmentIntf
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Фрагмент для отображения описания клумбы
 *
 * Created by i.repkina on 31.10.2021.
 */
class FlowerbedDescriptionFragment(): Fragment() {
    private lateinit var binding: FragmentFlowerbedDescriptionBinding
    private var flowerbedId: Long? = null

    private val viewModel by viewModels<FlowerbedDescriptionViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FlowerbedDescriptionViewModel(flowerbedId, dagger().getFlowerbedInteractor()) as T
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // подписка на life-data view-модели
        viewModel.flowerbedLiveData.observe(viewLifecycleOwner) { flowerbed ->
            Log.d(TAG, "onDataChanged() called with: flowerbed = $flowerbed")
            showFlowerbed(flowerbed)
            if (flowerbed.flowerbedId != null) {
                Log.d(
                    TAG,
                    "onDataChanged flowerbedId = ${flowerbed.flowerbedId}, : this.parentFragment = ${this.parentFragment}"
                )
                (this.parentFragment as? FlowerbedFragmentIntf)?.updateFlowerbedId(flowerbed.flowerbedId)
            }
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener() called")
            saveFlowerbed()
        }
    }

    private fun saveFlowerbed(){
        viewModel.onSaveData(
            flowerbedId,
            binding.name.text.toString(),
            binding.description.text.toString(),
            binding.comment.text.toString()
        )
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
        binding.name.setText(flowerbed?.name)
        binding.description.setText(flowerbed?.description)
        binding.comment.setText(flowerbed?.comment)
    }

    override fun onStop() {
        super.onStop()
        viewModel.onClose(flowerbedId,
            binding.name.text.toString(),
            binding.description.text.toString(),
            binding.comment.text.toString()
        )
    }
}

private const val TAG = "${APP_TAG}.FlowerbedDescriptionFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"