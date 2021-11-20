package com.irepka3.mygarden.ui.flowerbed.plants.list

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
import com.irepka3.mygarden.domain.model.Plant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Адаптер для работы со списком растений
 * @param callback коллбек для отображения карточки растения
 *
 * Created by i.repkina on 31.10.2021.
 */
class PlantListAdapter(val callback: PlantListAdapterCallback) :
    RecyclerView.Adapter<PlantListAdapter.PlantListViewHolder>() {
    private var diffUtilCallback = object : DiffUtil.ItemCallback<Plant>() {
        override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.plantId == newItem.plantId
        }

        override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.equals(newItem)
        }
    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    fun getItem(index: Int): Plant = listDiffer.currentList[index]

    /**
     * Обновление списка растений в адаптере
     * @param plantList новый список растений
     */
    fun updateItems(plantList: List<Plant>) {
        listDiffer.submitList(plantList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_plantlist_item, parent, false)
        return PlantListViewHolder(itemView, callback)
    }

    override fun onBindViewHolder(holder: PlantListViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    class PlantListViewHolder(itemView: View, val callback: PlantListAdapterCallback) :
        RecyclerView.ViewHolder(itemView) {
        private val plantNameTextView = itemView.findViewById<TextView>(R.id.textViewPlantName)
        private val plantDescriptionTextView =
            itemView.findViewById<TextView>(R.id.textViewPlantDescription)
        private val plantDateTextView = itemView.findViewById<TextView>(R.id.textViewPlantDate)
        private val plantCountTextView = itemView.findViewById<TextView>(R.id.textViewPlantCount)
        private val plantDefaultPhoto = itemView.findViewById<ImageView>(R.id.imageViewDefaultPlant)

        fun bind(item: Plant) {
            plantNameTextView.text = item.name
            plantDescriptionTextView.text = item.description
            if (item.datePlant !== null) {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val date = Date(item.datePlant)
                plantDateTextView.text = dateFormat.format(date)
            }
            plantCountTextView.text = item.count.toString()

            Log.d(
                TAG,
                "bind() called with: plantDefaultPhoto = $plantDefaultPhoto, item.uri = ${item.uri}"
            )
            if (item.uri !== null) {
                Glide.with(itemView.context)
                    .load(Uri.parse(item.uri).path)
                    .fitCenter()
                    .into(plantDefaultPhoto)
            }

            if (item.flowerbedId == 0L)
                throw IllegalStateException("Incorrect item.flowerbedId = ${item.flowerbedId}")

            if (item.plantId == 0L)
                throw IllegalStateException("Incorrect item.plantId = ${item.plantId}")

            itemView.setOnClickListener {
                callback.onPlantClick(
                    flowerbedId = item.flowerbedId,
                    plantId = item.plantId ?: 0L,
                    plantName = item.name
                )
            }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface PlantListAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранное растение
         * @param plantId идентификатор растения
         * @param plantName название растения
         */
        fun onPlantClick(flowerbedId: Long, plantId: Long?, plantName: String?)
    }
}

private const val TAG = "PlantListAdapter"