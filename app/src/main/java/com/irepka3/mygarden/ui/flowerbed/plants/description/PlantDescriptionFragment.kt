package com.irepka3.mygarden.ui.flowerbed.plants.description

import android.app.DatePickerDialog
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
import com.irepka3.mygarden.databinding.FragmentPlantDescriptionBinding
import com.irepka3.mygarden.domain.model.Plant
import com.irepka3.mygarden.ui.flowerbed.plants.PlantFragmentIntf
import com.irepka3.mygarden.util.Const
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by i.repkina on 05.11.2021.
 */
class PlantDescriptionFragment: Fragment() {
    private lateinit var binding: FragmentPlantDescriptionBinding
    private var flowerbedId: Long = 0L
    private var plantId: Long? = null
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val viewModel by viewModels<PlantDescriptionViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PlantDescriptionViewModel(
                    flowerbedId = flowerbedId,
                    plantId = plantId,
                    dagger().getPlantInteractor()
                ) as T
            }
        }
    }

    companion object {
        /**
         * Создает фрагмент для редактирования описания растения
         * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun newInstanceUpdate(flowerbedId: Long, plantId: Long): PlantDescriptionFragment {
            return PlantDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putLong(PLANT_ID, plantId)
                    putString(MODE, MODE_UPDATE)
                }
            }
        }

        /**
         * Создает фрагмент для добавления описания нового растения
         */
        fun newInstanceInsert(flowerbedId: Long): PlantDescriptionFragment {
            return PlantDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putLong(FLOWERBED_ID, flowerbedId)
                    putString(MODE,  MODE_INSERT) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView() called")
        binding = FragmentPlantDescriptionBinding.inflate(inflater)
        readArguments()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectDateView = binding.plantDate
        selectDateView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this.requireContext(),
                { _, newYear, newMonth, newDay ->
                        selectDateView.setText("$newDay.${newMonth + 1}.$newYear")
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        // подписка на life-data view-модели
        viewModel.plantLiveData.observe(viewLifecycleOwner) { plant ->
            Log.d(TAG, "onDataChanged() called with: plant = $plant")
            showPlant(plant)
            if (plant.plantId != null) {
                Log.d(TAG,
                    "onDataChanged plantId = ${plantId}, : this.parentFragment = ${this.parentFragment}"
                )
                (this.parentFragment as? PlantFragmentIntf)?.updatePlantId(plant.plantId)

            }
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            Log.d(TAG, "onViewCreated(), setOnClickListener called")
            savePlant()
        }
    }

    private fun readArguments() {
        Log.d(TAG, "readArguments() called")
        flowerbedId = arguments?.getLong(FLOWERBED_ID) ?: 0L
        if (flowerbedId == 0L)
            throw Exception("Incorrect arguments: flowerbedId = $flowerbedId")

        when (val mode = arguments?.getString(MODE)) {
            MODE_UPDATE -> {
                plantId = arguments?.getLong(PLANT_ID) ?: 0L
                if (plantId == 0L)
                    throw Exception("Incorrect arguments: mode = $mode, plantId = $plantId")
            }
            MODE_INSERT -> plantId = null
            else -> throw Exception("Incorrect arguments: plant mode: $mode")
        }
    }

    private fun showPlant(plant: Plant?){
        binding.name.setText(plant?.name)
        binding.description.setText(plant?.description)
        binding.comment.setText(plant?.comment)
        binding.count.setText(plant?.count.toString())

        if (plant?.datePlant !== null) {
            val date = Date(plant.datePlant)
            binding.plantDate.setText(dateFormat.format(date))
        }
    }

    private fun savePlant(){
        viewModel.onSaveData(
            flowerbedId = flowerbedId,
            plantId = plantId,
            binding.name.text.toString(),
            binding.description.text.toString(),
            binding.comment.text.toString(),
            binding.count.text.toString().toInt(),
            binding.plantDate.text.toString()
        )
    }

    override fun onStop() {
        super.onStop()
        viewModel.onClose(
            flowerbedId,
            plantId,
            binding.name.text.toString(),
            binding.description.text.toString(),
            binding.comment.text.toString(),
            binding.count.text.toString().toInt(),
            binding.plantDate.text.toString()
        )
    }
}

private const val TAG = "${Const.APP_TAG}.PlantDescriptionFragment"
private const val FLOWERBED_ID = "flowerbedId"
private const val PLANT_ID = "plantId"
private const val MODE = "mode"
private const val MODE_INSERT = "insert"
private const val MODE_UPDATE = "update"