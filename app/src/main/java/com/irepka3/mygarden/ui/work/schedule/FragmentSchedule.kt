package com.irepka3.mygarden.ui.work.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.FragmentScheduleBinding
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.util.Const

/**
 * Фрагмент для расписания
 *
 * Created by i.repkina on 12.11.2021.
 */
class FragmentSchedule : Fragment() {
    private lateinit var schedule: ScheduleUIModel
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater)
        readArguments()
        val scheduleData = schedule

        initToolBar()

        val months = resources.getStringArray(R.array.period_month_array)
        val monthAdapter = ArrayAdapter(requireContext(), R.layout.spinner_list_item, months)
        with(binding.month.editText as AutoCompleteTextView) {
            setAdapter(monthAdapter)
            setText(months[scheduleData.schedule.month ?: 0], false)
        }

        val weeks = resources.getStringArray(R.array.period_week_array)
        val weekAdapter = ArrayAdapter(requireContext(), R.layout.spinner_list_item, weeks)

        with(binding.week.editText as AutoCompleteTextView) {
            setAdapter(weekAdapter)
            setText(weeks[scheduleData.schedule.week ?: 0], false)
        }

        with(binding) {
            checkBoxDay1.isChecked = scheduleData.schedule.monday
            checkBoxDay2.isChecked = scheduleData.schedule.tuesday
            checkBoxDay3.isChecked = scheduleData.schedule.wednesday
            checkBoxDay4.isChecked = scheduleData.schedule.thursday
            checkBoxDay5.isChecked = scheduleData.schedule.friday
            checkBoxDay6.isChecked = scheduleData.schedule.saturday
            checkBoxDay7.isChecked = scheduleData.schedule.sunday

            saveButton.setOnClickListener {
                val result = Bundle()

                val selectedMonth = months.indexOf(month.editText?.text.toString())
                schedule.schedule.month = selectedMonth.takeIf { selectedMonth > 0 }

                val selectedWeek = weeks.indexOf(week.editText?.text.toString())
                schedule.schedule.week = selectedWeek.takeIf { selectedWeek > 0 }


                schedule.schedule.monday = checkBoxDay1.isChecked
                schedule.schedule.tuesday = checkBoxDay2.isChecked
                schedule.schedule.wednesday = checkBoxDay3.isChecked
                schedule.schedule.thursday = checkBoxDay4.isChecked
                schedule.schedule.friday = checkBoxDay5.isChecked
                schedule.schedule.saturday = checkBoxDay6.isChecked
                schedule.schedule.sunday = checkBoxDay7.isChecked

                result.putSerializable("schedule", schedule)
                parentFragmentManager.setFragmentResult("schedule", result)
                requireActivity().onBackPressed()
            }
        }

        return binding.root
    }

    private fun initToolBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            val actionBar = supportActionBar
            actionBar?.title = resources.getString(R.string.schedule_caption)

            actionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
            actionBar?.setDisplayShowHomeEnabled(supportFragmentManager.backStackEntryCount > 0)

            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun readArguments() {
        Log.d(TAG, "readArguments() called")
        schedule = arguments?.getSerializable(SCHEDULE) as ScheduleUIModel
        Log.d(TAG, "readArguments() called scheduleId = ${schedule}")
    }

    companion object {
        /**
         * Создает фрагмент для создания и редактирования расписания
         * @param schedule UI модель расписания
         */
        fun newInstance(schedule: ScheduleUIModel?): FragmentSchedule {
            return FragmentSchedule().apply {
                arguments = Bundle().apply {
                    putSerializable(SCHEDULE, schedule)
                }
            }
        }
    }
}


private const val TAG = "${Const.APP_TAG}.FragmentSchedule"
private const val SCHEDULE = "schedule"