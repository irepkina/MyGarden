package com.irepka3.mygarden.ui.work.schedule

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.ui.work.description.model.ScheduleUIModel
import com.irepka3.mygarden.util.Const


/**
 * Адаптер для работы со списком расписаний
 *
 * Created by i.repkina on 12.11.2021.
 */
class ScheduleAdapter(val callback: scheduleAdapterCallback) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    private var diffUtilCallback = object : DiffUtil.ItemCallback<ScheduleUIModel>() {
        override fun areItemsTheSame(oldItem: ScheduleUIModel, newItem: ScheduleUIModel): Boolean {
            return (newItem.schedule.scheduleId != null && oldItem.schedule.scheduleId == newItem.schedule.scheduleId) ||
                    oldItem.number == newItem.number
        }

        override fun areContentsTheSame(
            oldItem: ScheduleUIModel,
            newItem: ScheduleUIModel
        ): Boolean {
            return oldItem == newItem
        }
    }


    /**
     * Обновление списка расписаний в адаптере
     * @param sheduleList новый список расписаний
     */
    fun updateItems(sheduleList: List<ScheduleUIModel>) {
        listDiffer.submitList(sheduleList.sortedBy { it.number })
    }

    // получить элемент по индексу
    fun getItem(index: Int): ScheduleUIModel = listDiffer.currentList[index]

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_schedule_item, parent, false)
        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.textViewScheduleDescription)
        private val day1 = itemView.context.resources.getString(R.string.day1_text_view_text)
        private val day2 = itemView.context.resources.getString(R.string.day2_text_view_text)
        private val day3 = itemView.context.resources.getString(R.string.day3_text_view_text)
        private val day4 = itemView.context.resources.getString(R.string.day4_text_view_text)
        private val day5 = itemView.context.resources.getString(R.string.day5_text_view_text)
        private val day6 = itemView.context.resources.getString(R.string.day6_text_view_text)
        private val day7 = itemView.context.resources.getString(R.string.day7_text_view_text)
        private val weekPostFix = itemView.context.resources.getString(R.string.week_schedule_text)
        private val weeks = itemView.context.resources.getStringArray(R.array.period_week_array)
        private val months = itemView.context.resources.getStringArray(R.array.period_month_array)

        fun bind(item: ScheduleUIModel) {
            Log.d(TAG, "bind() called with: item = $item")
            val week = item.schedule.week ?: 0
            val month = item.schedule.month ?: 0


            val text = "${months[month]} , ${weeks[week]} $weekPostFix, " +
                    "${if (item.schedule.monday) "$day1 , " else ""}" +
                    "${if (item.schedule.tuesday) "$day2 , " else ""}" +
                    "${if (item.schedule.wednesday) "$day3 , " else ""}" +
                    "${if (item.schedule.thursday) "$day4 , " else ""}" +
                    "${if (item.schedule.friday) "$day5 , " else ""}" +
                    "${if (item.schedule.saturday) "$day6 , " else ""}" +
                    "${if (item.schedule.sunday) "$day7 , " else ""}"
            textView.text = text.removeSuffix(", ")
            Log.d(TAG, "bind(), text = $text, tuesday = ${item.schedule.tuesday}, day2 = $day2")
            itemView.setOnClickListener { callback.onScheduleClick(item) }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface scheduleAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранное расписание
         * @param scheduleData UI модель расписания
         */
        fun onScheduleClick(scheduleData: ScheduleUIModel)
    }
}

private const val TAG = "${Const.APP_TAG}.ScheduleAdapter"