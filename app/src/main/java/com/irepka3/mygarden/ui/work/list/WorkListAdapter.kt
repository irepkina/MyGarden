package com.irepka3.mygarden.ui.work.list


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
import com.irepka3.mygarden.ui.util.context.themeColor
import com.irepka3.mygarden.ui.work.model.WorkUIId
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

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

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
        private val viewName = itemView.findViewById<TextView>(R.id.textViewWorkName)
        private val viewDescription = itemView.findViewById<TextView>(R.id.textViewWorkDescription)
        private val viewDay = itemView.findViewById<TextView>(R.id.textViewDay)
        private val viewWeek = itemView.findViewById<TextView>(R.id.textViewWeek)
        private val viewWorkStatus = itemView.findViewById<ImageView>(R.id.imageViewStatus)
        private val cardView = itemView.findViewById<CardView>(R.id.cardView)
        private val cardViewColor = itemView.context.themeColor(R.attr.colorWorkCard)
        private val cardViewColorCancel = itemView.context.themeColor(R.attr.colorWorkCardDisabled)

        fun bind(item: Work) {
            viewName.text = item.name

            viewDay.text = SimpleDateFormat(
                "dd",
                Locale.getDefault()
            ).format(item.datePlan)
            viewWeek.text = SimpleDateFormat(
                "E",
                Locale.getDefault()
            ).format(item.datePlan)
            viewDescription.text = item.description

            viewWorkStatus.setImageLevel(
                when {
                    item.status == WorkStatus.Done -> 2
                    item.status == WorkStatus.Cancel -> 3
                    item.repeatWork?.repeatWorkId != null -> 1
                    else -> 0
                }
            )

            cardView.setCardBackgroundColor(if (item.status == WorkStatus.Cancel) cardViewColorCancel else cardViewColor)

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