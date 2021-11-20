package com.irepka3.mygarden.ui.flowerbed.list

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irepka3.mygarden.R
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.util.Const.APP_TAG

/**
 * Адаптер для работы со списком клумб
 * @param callback коллбек для отображения карточки клумбы
 *
 * Created by i.repkina on 31.10.2021.
 */
class FlowerbedListAdapter(private val callback: FlowerbedAdapterCallback) :
    RecyclerView.Adapter<FlowerbedListAdapter.FlowerbedViewHolder>() {
    private var diffUtilCallback = object : DiffUtil.ItemCallback<Flowerbed>() {
        override fun areItemsTheSame(oldItem: Flowerbed, newItem: Flowerbed): Boolean {
            return oldItem.flowerbedId == newItem.flowerbedId
        }

        override fun areContentsTheSame(oldItem: Flowerbed, newItem: Flowerbed): Boolean {
            return oldItem == newItem
        }
    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    // получить элемент по индексу
    fun getItem(index: Int): Flowerbed = listDiffer.currentList[index]

    /**
     * Обновление списка клумб в адаптере
     * @param flowerbedList новый список клумб
     */
    fun updateItems(flowerbedList: List<Flowerbed>) {
        listDiffer.submitList(flowerbedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerbedViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_flowerbedlist_item, parent, false)
        return FlowerbedViewHolder(itemView, callback)
    }

    override fun onBindViewHolder(holder: FlowerbedViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    class FlowerbedViewHolder(itemView: View, private val callback: FlowerbedAdapterCallback) :
        RecyclerView.ViewHolder(itemView) {
        private val flowerbedNameTextView =
            itemView.findViewById<TextView>(R.id.textViewFlowerbedName)
        private val flowerbedDescriptionTextView =
            itemView.findViewById<TextView>(R.id.textViewFlowerbedDescription)
        private val flowerbedDefaultPhoto =
            itemView.findViewById<ImageView>(R.id.imageViewDefaultFlowerbed)

        fun bind(item: Flowerbed) {
            flowerbedNameTextView.text = item.name
            flowerbedDescriptionTextView.text = item.description
            Log.d(
                TAG,
                "bind() called with: flowerbedDefaultPhoto = $flowerbedDefaultPhoto, item.uri = ${item.uri}"
            )
            if (item.uri !== null) {
                Glide.with(itemView.context)
                    .load(Uri.parse(item.uri).path)
                    .fitCenter()
                    .into(flowerbedDefaultPhoto)
            }

            if (item.flowerbedId == 0L)
                throw IllegalStateException("Incorrect item.flowerbedId = ${item.flowerbedId}")

            itemView.setOnClickListener {
                callback.onFlowerbedClick(
                    item.flowerbedId ?: 0L,
                    item.name
                )
            }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface FlowerbedAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранную клумбу
         * @param flowerbedId идентификатор выбранной клумбы
         * @param flowerbedName название клумбы
         */
        fun onFlowerbedClick(flowerbedId: Long, flowerbedName: String)
    }
}

private const val TAG = "${APP_TAG}.FlowerbedListAdapter"