package com.irepka3.mygarden.ui.work.description

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.databinding.FragmentWorkManagerBinding
import com.irepka3.mygarden.domain.model.Schedule
import com.irepka3.mygarden.domain.model.WorkStatus
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkUIState
import com.irepka3.mygarden.ui.work.model.WorkUIId
import com.irepka3.mygarden.ui.work.schedule.ScheduleAdapter
import com.irepka3.mygarden.util.Const
import java.util.Calendar

/**
 * Фрагмент для настройки работы
 *
 * Created by i.repkina on 11.11.2021.
 */
class FragmentWorkMamager : Fragment(), ScheduleAdapter.scheduleAdapterCallback {
    private lateinit var binding: FragmentWorkManagerBinding

    // идентификатор работы
    private lateinit var workUIId: WorkUIId

    private val adapter = ScheduleAdapter(this)

    private val viewModel by viewModels<WorkManagerViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WorkManagerViewModel(
                    dagger().getWorkManagerInteractor()
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkManagerBinding.inflate(inflater)

        readArguments()

        val itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        val drawable = this.resources.getDrawable(
            R.drawable.recycleview_divider_transparent,
            this.activity?.theme
        )
        itemDecorator.setDrawable(drawable)
        binding.scheduleRecyclerView.addItemDecoration(itemDecorator)

        binding.scheduleRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        binding.scheduleRecyclerView.adapter = adapter

        viewModel.onCreateView(workUIId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("schedule") { key, bundle ->
            Log.d(TAG, "fragmentResultListener: called with: key = $key, bundle = $bundle")
            val schedule = bundle.getSerializable("schedule") as ScheduleUIModel
            Log.d(TAG, "fragmentResultListener: schedule = $schedule")
            viewModel.onUpdateSchedule(schedule)
        }

        viewModel.workLiveData.observe(viewLifecycleOwner) { workData ->
            Log.d(TAG, "onDataChanged() called with: workData = $workData")
            showWork(workData)
        }

        viewModel.workUIStateLiveData.observe(viewLifecycleOwner) { workUIState ->
            Log.d(TAG, "onDataChanged() called with: workUIState = $workUIState")
            updateUIState(workUIState)
        }
        viewModel.scheduleLiveData.observe(viewLifecycleOwner) { scheduleData ->
            Log.d(TAG, "onDataChanged() called with: scheduleData = $scheduleData")
            if (scheduleData != null) {
                adapter.updateItems(scheduleData)
            }
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.commandLiveData.observe(viewLifecycleOwner) { command ->
            when (command) {
                Command.CLOSE_VIEW -> requireActivity().onBackPressed()
            }
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.e(TAG, "onCreateView() called with: error = ${error.message}", error)
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener() called")
            saveWork()
        }

        binding.checkBoxNoNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.notificationLayout.isVisible = !isChecked
        }

        binding.floatingButtonAddSchedule.setOnClickListener {
            (requireActivity() as MainActivityIntf).showScheduleFragment(
                ScheduleUIModel(
                    Schedule(
                        repeatWorkId = workUIId.repeatWorkId
                    ), null
                )
            )
        }

        binding.planDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { _, newYear, newMonth, newDay ->
                    binding.planDate.setText("$newDay.${newMonth + 1}.$newYear")
                },
                year, month, day
            )
            datePickerDialog.show()
        }

    }

    private fun showScheduleLayout(isShow: Boolean) {
        if (isShow) {
            with(binding) {
                planDate.isVisible = false
                scheduleLayout.isVisible = true
            }
        } else {
            with(binding) {
                planDate.isVisible = true
                scheduleLayout.isVisible = false
            }
        }
    }

    private fun readArguments() {
        workUIId = arguments?.getSerializable(WORK_UI_ID) as WorkUIId
        Log.d(TAG, "readArguments() called workUI = $workUIId")
    }

    private fun updateUIState(workUIState: WorkUIState) {
        with(binding) {
            switchWorkType.isEnabled = workUIState.isRepeatSwitcherEnabled
            name.isEnabled = !workUIState.isReadOnly
            planDate.isEnabled = !workUIState.isReadOnly
            notificationDayCount.isEnabled = !workUIState.isReadOnly
            notificationHour.isEnabled = !workUIState.isReadOnly
            notificationMinute.isEnabled = !workUIState.isReadOnly
            checkBoxNoNotification.isEnabled = !workUIState.isReadOnly
            floatingButtonAddSchedule.isEnabled = !workUIState.isReadOnly

            doneButton.isVisible = workUIState.isDoneEnabled
            cancelButton.isVisible = workUIState.isCancelEnabled
            clearStatusButton.isVisible = workUIState.isClearStatusEnabled
            originalButton.isVisible = workUIState.isShowOriginRepeatEnabled

            if (workUIState.isEditMode) {
                (requireActivity() as MainActivityIntf).setCaption(workUIState.workTitle)
            } else {
                (requireActivity() as MainActivityIntf).setCaption(resources.getString(R.string.new_work_caption))
            }
        }
    }

    private fun showWork(work: WorkUIModel) {
        with(binding) {
            name.setText(work.name)
            description.setText(work.description)
            planDate.setText(work.datePlan)

            //dateDone = null todo реализовать отметку выполнения
            //WorkStatus.Plan  todo реализовать логику изменения статуса
            notificationDayCount.setText(work.notificationDay.toString())
            notificationHour.setText(work.notificationHour.toString())
            notificationMinute.setText(work.notificationMinute.toString())
            checkBoxNoNotification.isChecked = work.noNotification
            notificationLayout.isVisible = !work.noNotification

            switchWorkType.setOnCheckedChangeListener(null)
            switchWorkType.isChecked = work.repeatWorkId != null || !work.isOnceWork

            if (work.workId == null && work.repeatWorkId == null) {
                showScheduleLayout(switchWorkType.isChecked)
                switchWorkType.setOnCheckedChangeListener { buttonView, isChecked ->
                    showScheduleLayout(isChecked)
                }
            } else {
                showScheduleLayout(work.workId == work.repeatWorkId)
            }


            originalButton.setOnClickListener {
                if (work.repeatWorkId != null) {
                    (requireActivity() as MainActivityIntf).showWorkManagerFragment(
                        WorkUIId(
                            workId = work.repeatWorkId,
                            repeatWorkId = work.repeatWorkId,
                            planDate = null
                        )
                    )
                }
            }
        }
    }

    private fun saveWork() {
        val name = binding.name.text.toString()
        if (name.isBlank()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.no_work_name_toast_text),
                Toast.LENGTH_LONG
            ).show()
        } else {
            viewModel.onSaveData(createWorkUIModel())
        }
    }

    override fun onDestroyView() {
        viewModel.onStoreData(createWorkUIModel())
        super.onDestroyView()
    }

    private fun createWorkUIModel(): WorkUIModel {
        return WorkUIModel(
            isOnceWork = !binding.switchWorkType.isChecked,
            repeatWorkId = workUIId.repeatWorkId,
            workId = workUIId.workId,
            name = binding.name.text.toString(),
            description = binding.description.text.toString(),
            datePlan = binding.planDate.text.toString(),
            dateDone = null, //todo реализовать отметку выполнения
            status = WorkStatus.Plan, //todo реализовать логику изменения статуса

            // todo исправить, binding.notificationDayCount.text.toString() != "null"
            notificationDay = if (binding.notificationDayCount.text.toString() != "null") {
                binding.notificationDayCount.text.toString().toInt()
            } else null,
            notificationHour = if (binding.notificationHour.text.toString() != "null") {
                binding.notificationHour.text.toString().toInt()
            } else null,
            notificationMinute = if (binding.notificationMinute.text.toString() != "null") {
                binding.notificationMinute.text.toString().toInt()
            } else null,
            noNotification = binding.checkBoxNoNotification.isChecked
        )
    }

    override fun onScheduleClick(scheduleData: ScheduleUIModel) {
        (requireActivity() as MainActivityIntf).showScheduleFragment(scheduleData)
    }

    companion object {
        /**
         * Создает фрагмент для создания и редактирования работы
         * @param workUIId идентификатор работы
         */
        fun newInstance(workUIId: WorkUIId): FragmentWorkMamager {
            return FragmentWorkMamager().apply {
                arguments = Bundle().apply {
                    putSerializable(WORK_UI_ID, workUIId)
                }
            }
        }
    }
}

private const val TAG = "${Const.APP_TAG}.FragmentWorkMamager"
private const val WORK_UI_ID = "WorkUI"