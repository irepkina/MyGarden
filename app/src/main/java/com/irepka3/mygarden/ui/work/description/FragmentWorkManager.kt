package com.irepka3.mygarden.ui.work.description

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
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
import com.irepka3.mygarden.ui.util.edittext.SimpleTextWatcher
import com.irepka3.mygarden.ui.util.recycleView.ItemTouchHelperFactory
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.ui.work.description.model.WorkTypeUI
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
class FragmentWorkManager : Fragment(), ScheduleAdapter.scheduleAdapterCallback {
    private lateinit var binding: FragmentWorkManagerBinding

    // идентификатор работы
    private lateinit var workUIId: WorkUIId

    private val adapter = ScheduleAdapter(this)
    private val nameWatcher = SimpleTextWatcher() { binding.name.error = null }
    private val dataChangeWatcher = SimpleTextWatcher() { viewModel.onDataChanged() }


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
        initToolBar()

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
        viewModel.isDataChangedLiveData.observe(viewLifecycleOwner) { result ->
            binding.saveButton.isVisible = result
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

        with(binding) {
            saveButton.setOnClickListener {
                Log.d(TAG, "setOnClickListener() called")
                saveWork()
            }

            /* todo: Восстановить после реализации нотификации
            checkBoxNoNotification.setOnCheckedChangeListener { buttonView, isChecked ->
                notificationLayout.isVisible = !isChecked
            }*/

            buttonAddSchedule.setOnClickListener {
                (requireActivity() as MainActivityIntf).showScheduleFragment(
                    ScheduleUIModel(
                        Schedule(
                            repeatWorkId = workUIId.repeatWorkId
                        ), null
                    )
                )
            }

            doneButton.setOnClickListener { viewModel.onDone(createWorkUIModel()) }
            cancelButton.setOnClickListener { viewModel.onCancel(createWorkUIModel()) }
            clearStatusButton.setOnClickListener { viewModel.onClear(createWorkUIModel()) }

            planDate.editText?.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    root.context,
                    { _, newYear, newMonth, newDay ->
                        planDate.editText?.setText("$newDay.${newMonth + 1}.$newYear")
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }

            name.editText?.doOnTextChanged { _, _, _, _ ->
                name.error = null
            }
        }

        val itemTouchHelper = ItemTouchHelperFactory.getItemTouchHelper { adapterPosition ->
            deleteSchedule(adapter.getItem(adapterPosition))
        }
        itemTouchHelper.attachToRecyclerView(binding.scheduleRecyclerView)

    }

    private fun initToolBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            val actionBar = supportActionBar

            actionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
            actionBar?.setDisplayShowHomeEnabled(supportFragmentManager.backStackEntryCount > 0)

            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
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
            buttonAddSchedule.isEnabled = !workUIState.isReadOnly

            name.isVisible =
                workUIState.workTypeUI != WorkTypeUI.GENERATED_REPEAT && workUIState.workTypeUI != WorkTypeUI.SAVED_REPEAT

            doneButton.isVisible = workUIState.isDoneEnabled
            cancelButton.isVisible = workUIState.isCancelEnabled
            clearStatusButton.isVisible = workUIState.isClearStatusEnabled
            originalButton.isVisible = workUIState.isShowOriginRepeatEnabled

            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                if (workUIState.isEditMode) workUIState.workTitle
                else resources.getString(R.string.new_work_caption)
        }
    }

    private fun showWork(work: WorkUIModel) {
        removeWatchers()
        with(binding) {
            name.editText?.setText(work.name)
            name.error = null
            description.editText?.setText(work.description)
            planDate.editText?.setText(work.datePlan)

            notificationDayCount.editText?.setText(work.notificationDay.toString())
            notificationHour.editText?.setText(work.notificationHour.toString())
            notificationMinute.editText?.setText(work.notificationMinute.toString())
            checkBoxNoNotification.isChecked =
                false //todo: Восстановить после реализации нотификации work.noNotification
            notificationLayout.isVisible =
                false //todo: Восстановить после реализации нотификации !work.noNotification

            switchWorkType.setOnCheckedChangeListener(null)
            switchWorkType.isChecked = work.repeatWorkId != null || !work.isOnceWork

            workStatusImageView.setImageLevel(
                when {
                    work.status == WorkStatus.Done -> 2
                    work.status == WorkStatus.Cancel -> 3
                    work.repeatWorkId != null -> 1
                    else -> 0
                }
            )

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
        addWatchers()
    }

    private fun addWatchers() {
        binding.name.editText?.addTextChangedListener(dataChangeWatcher)
        binding.name.editText?.addTextChangedListener(nameWatcher)
        binding.description.editText?.addTextChangedListener(dataChangeWatcher)
        binding.planDate.editText?.addTextChangedListener(dataChangeWatcher)
        binding.notificationDayCount.editText?.addTextChangedListener(dataChangeWatcher)
        binding.notificationHour.editText?.addTextChangedListener(dataChangeWatcher)
        binding.notificationMinute.editText?.addTextChangedListener(dataChangeWatcher)
    }

    private fun removeWatchers() {
        binding.name.editText?.removeTextChangedListener(dataChangeWatcher)
        binding.name.editText?.removeTextChangedListener(nameWatcher)
        binding.description.editText?.removeTextChangedListener(dataChangeWatcher)
        binding.planDate.editText?.removeTextChangedListener(dataChangeWatcher)
        binding.notificationDayCount.editText?.removeTextChangedListener(dataChangeWatcher)
        binding.notificationHour.editText?.removeTextChangedListener(dataChangeWatcher)
        binding.notificationMinute.editText?.removeTextChangedListener(dataChangeWatcher)
    }

    private fun saveWork() {
        val workUIModel = createWorkUIModel()
        if (workUIModel.name?.isBlank() == true) {
            binding.name.error = resources.getString(R.string.error_empty_name)
        } else {
            viewModel.onSaveData(workUIModel)
        }
    }

    /**
     * Удаление расписания для сохраненной работы
     */
    private fun deleteSchedule(schedule: ScheduleUIModel) {
        viewModel.onDeleteSchedule(schedule)
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
            name = binding.name.editText?.text.toString(),
            description = binding.description.editText?.text.toString(),
            datePlan = binding.planDate.editText?.text.toString(),
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
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
        fun newInstance(workUIId: WorkUIId): FragmentWorkManager {
            return FragmentWorkManager().apply {
                arguments = Bundle().apply {
                    putSerializable(WORK_UI_ID, workUIId)
                }
            }
        }
    }
}

private const val TAG = "${Const.APP_TAG}.FragmentWorkManager"
private const val WORK_UI_ID = "WorkUI"