package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.irepka3.mygarden.R
import com.irepka3.mygarden.ui.photo.model.Photo
import com.irepka3.mygarden.util.Const.APP_TAG


/**
 * Адаптер для работы со списком фото
 *
 * Created by i.repkina on 07.11.2021.
 */
class BasePhotoListAdapter(val callback: BasePhotoListAdapterCallback) :
    RecyclerView.Adapter<BasePhotoListAdapter.BasePhotoListViewHolder>() {

    private var diffUtilCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.photoId == newItem.photoId
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.equals(newItem)
        }
    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)
    var adapterMode = EditMode.Default
        set(value) {
            if (field != value) {
                for (position in listDiffer.currentList.indices) {
                    listDiffer.currentList[position].selected = false
                    notifyItemChanged(position, PAYLOAD_ADAPTER_MODE)
                }
                field = value
                callback.setEditMode(value)
            }
        }


    /**
     * Обновление списка фотографий в адаптере
     * @param photoList новый список фото
     */
    fun updateItems(photoList: List<Photo>) {
        listDiffer.submitList(photoList)
    }

    /**
     * Возвращает список фото, которые нужно удалить
     */
    fun getPhotoToDelete(): List<Photo> {
        val listToDeleteRes = mutableListOf<Photo>()
        val newList = listDiffer.currentList
        for (photo in newList) {
            if (photo.selected) {
                listToDeleteRes.add(photo)
                photo.selected = false
            }
        }
        listDiffer.submitList(newList)
        adapterMode = EditMode.Default
        return listToDeleteRes
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasePhotoListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_photo_list_item, parent, false)

        return BasePhotoListViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: BasePhotoListViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                // обновляем только чекбоксы, чтобы картиники не грузились
                if (payload == PAYLOAD_ADAPTER_MODE) {
                    holder.bindPayload(listDiffer.currentList[position])
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: BasePhotoListViewHolder, position: Int) {
        val currentList = listDiffer.currentList
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    inner class BasePhotoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView = itemView.findViewById<ImageView>(R.id.imageViewPhoto)
        val checkBox = itemView.findViewById<CheckBox>(R.id.photoCheckBox)

        fun bind(item: Photo) {
            Glide.with(itemView.context)
                .load(Uri.parse(item.uri).path)
                .override(300, 300)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoImageView)

            checkBox.isVisible = adapterMode == EditMode.DeleteMode
            checkBox.isChecked = item.selected

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.selected = isChecked
            }

            photoImageView.setOnClickListener {
                // При клике на фото сбрасываем режим на дефолтный, если был режим удаления
                // Если был дефолтный режим, то открываем фото на просмотр
                Log.d(TAG, "bind(), setOnClickListener called")
                if (adapterMode == EditMode.DeleteMode) {
                    adapterMode = EditMode.Default
                } else {
                    if (item.photoId == null) {
                        throw IllegalStateException("Invalid photoId = null")
                    } else {
                        callback.photoClick(listDiffer.currentList.indexOf(item))
                    }
                }

            }


            photoImageView.setOnLongClickListener {
                // При долгом нажатии на фото переходим в режим удаления
                Log.d(TAG, "bind() called, setOnLongClickListener")
                adapterMode = EditMode.DeleteMode
                true
            }
        }

        fun bindPayload(item: Photo) {
            Log.d(TAG, "bind() called with: item.uri = ${item.uri}")
            checkBox.isVisible = adapterMode == EditMode.DeleteMode
            checkBox.isChecked = item.selected
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface BasePhotoListAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранное фото
         * @param photoPosition номер фото в списке
         */
        fun photoClick(photoPosition: Int)
        fun setEditMode(editMode: EditMode)
    }
}

enum class EditMode {
    DeleteMode,
    Default
}

private const val TAG = "${APP_TAG}.BasePhotoListAdapter"
private const val PAYLOAD_ADAPTER_MODE = "payload_adapter_mode"