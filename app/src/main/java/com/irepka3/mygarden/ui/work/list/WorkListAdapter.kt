package com.irepka3.mygarden.ui.work.list


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
import com.irepka3.mygarden.ui.work.model.WorkUIId
import com.irepka3.mygarden.util.Const
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Адаптер для работы со списком работ
 *
 * Created by i.repkina on 10.11.2021.
 */
class WorkListAdapter(val callback: WorkListAdapterCallback) :
    RecyclerView.Adapter<WorkListAdapter.WorkViewHolder>() {

    private var diffUtilCallback = object : DiffUtil.ItemCallback<Work>() {
        override fun areItemsTheSame(oldItem: Work, newItem: Work): Boolean {
            return oldItem.workId == newItem.workId
        }

        override fun areContentsTheSame(oldItem: Work, newItem: Work): Boolean {
            return oldItem == newItem
        }
    }

    private val listDiffer = AsyncListDiffer<Work>(this, diffUtilCallback)

    // получить элемент по индексу
    fun getItem(index: Int): Work = listDiffer.currentList[index]


    /**
     * Обновление списка работ в адаптере
     * @param workList новый список работ
     */
    fun updateItems(workList: List<Work>) {
        listDiffer.submitList(workList)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkListAdapter.WorkViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_worklist_item, parent, false)
        return WorkViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkListAdapter.WorkViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    inner class WorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewName = itemView.findViewById<TextView>(R.id.textViewWorkName)
        val viewDescription = itemView.findViewById<TextView>(R.id.textViewWorkDescription)
        val viewWeekDay = itemView.findViewById<TextView>(R.id.textViewWeekDay)
        val viewMonthYear = itemView.findViewById<TextView>(R.id.textViewMonthYear)
        val viewWorkStatus = itemView.findViewById<TextView>(R.id.textViewWorkStatus)

        fun bind(item: Work) {
            viewName.setText(item.name)


            viewWeekDay.setText(
                SimpleDateFormat(
                    "E, dd",
                    Locale.getDefault()
                ).format(item.datePlan)
            )
            viewMonthYear.setText(
                SimpleDateFormat(
                    "MMMM, yyyy",
                    Locale.getDefault()
                ).format(item.datePlan)
            )
            viewDescription.setText(item.description)

            viewWorkStatus.setText(
                when (item.status) {
                    WorkStatus.Plan -> "Запланировано"
                    WorkStatus.Done -> "Выполнено"
                    WorkStatus.Cancel -> "Отменено"
                    else -> "Новая работа"
                }
            )

            itemView.setOnClickListener {
                callback.onWorkClick(
                    WorkUIId(
                        workId = item.workId,
                        repeatWorkId = item.repeatWork?.repeatWorkId,
                        planDate = item.datePlan
                    )
                )
            }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface WorkListAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранную работу
         * @param workUIId идентификаторы выбранной работы
         */
        fun onWorkClick(workUIId: WorkUIId)
    }
}

private const val TAG = "${Const.APP_TAG}.WorkListAdapter"