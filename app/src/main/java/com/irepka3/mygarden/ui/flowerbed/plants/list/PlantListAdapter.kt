package com.irepka3.mygarden.ui.flowerbed.plants.list
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.domain.model.Plant

/**
 * Адаптер для работы со списком растений
 * @param callback коллбек для отображения карточки растения
 *
 * Created by i.repkina on 31.10.2021.
 */
class PlantListAdapter(val callback: PlantListAdapterCallback) : RecyclerView.Adapter<PlantListAdapter.PlantListViewHolder>() {
    private var diffUtilCallback = object : DiffUtil.ItemCallback<Plant>() {
        override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.plantId == newItem.plantId
        }

        override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.equals(newItem)
        }
    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    fun getItem(index: Int ): Plant = listDiffer.currentList[index]

    /**
     * Обновление списка растений в адаптере
     * @param plantList новый список растений
     */
    fun updateItems(plantList: List<Plant>){
        listDiffer.submitList(plantList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.layout_plant_item, parent, false)
        return PlantListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlantListViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    inner class PlantListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantTextView = itemView.findViewById<TextView>(R.id.textViewPlant)

        fun bind(item: Plant) {
            plantTextView.text = item.name
            plantTextView.setOnClickListener { callback.onPlantClick(item.plantId) }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface PlantListAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранное растение
         * @param plantId идентификатор растения
         */
        fun onPlantClick(plantId: Long?)
    }
}

private const val TAG = "PlantListAdapter"