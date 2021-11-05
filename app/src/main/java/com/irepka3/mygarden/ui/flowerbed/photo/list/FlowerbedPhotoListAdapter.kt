package com.irepka3.mygarden.ui.flowerbed.photo.list
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.domain.model.FlowerbedPhoto
import com.irepka3.mygarden.util.Const.APP_TAG
import java.io.File

/**
 * Адаптер для работы со списком фото клумбы
 * @param callback коллбек для отображения увеличенного фото клумбы
 *
 * Created by i.repkina on 04.11.2021.
 */
class FlowerbedPhotoListAdapter(val callback: FlowerbedPhotoListAdapterCallback) : RecyclerView.Adapter<FlowerbedPhotoListAdapter.FlowerbedPhotoListViewHolder>() {
    private var diffUtilCallback = object : DiffUtil.ItemCallback<FlowerbedPhoto>() {
        override fun areItemsTheSame(oldItem: FlowerbedPhoto, newItem: FlowerbedPhoto): Boolean {
            return oldItem.flowerbedPhotoId == newItem.flowerbedPhotoId
        }

        override fun areContentsTheSame(oldItem: FlowerbedPhoto, newItem: FlowerbedPhoto): Boolean {
            return oldItem.equals(newItem)
        }
    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    fun getItem(index: Int ): FlowerbedPhoto = listDiffer.currentList[index]

    /**
     * Обновление списка фотографий клумб в адаптере
     * @param flowerbedPhototList новый список фото клумб
     */
    fun updateItems(flowerbedPhototList: List<FlowerbedPhoto>){
        listDiffer.submitList(flowerbedPhototList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerbedPhotoListViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.layout_flowerbed_photo_item, parent, false)
        return FlowerbedPhotoListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FlowerbedPhotoListViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    inner class FlowerbedPhotoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flowerbedPhotoImageView = itemView.findViewById<ImageView>(R.id.imageViewPhoto)

        fun bind(item: FlowerbedPhoto) {
            Log.d(TAG, "bind() called with: item.uri = ${item.uri}")
            flowerbedPhotoImageView.setImageURI(Uri.parse(item.uri))
            flowerbedPhotoImageView.setOnClickListener { callback.onFlowerbedPhotoClick(item.flowerbedPhotoId) }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface FlowerbedPhotoListAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранное фото клумбы
         * @param flowerbedPhotoId идентификатор фото клумбы
         */
        fun onFlowerbedPhotoClick(flowerbedPhotoId: Long?)
    }
}

private const val TAG = "${APP_TAG}.FlowerbedPhotoListAdapter"