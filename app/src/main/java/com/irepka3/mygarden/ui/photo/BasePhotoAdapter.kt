package com.irepka3.mygarden.ui.flowerbed.photo.photo

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irepka3.mygarden.R
import com.irepka3.mygarden.ui.photo.model.Photo
import com.irepka3.mygarden.util.Const

/**
 * Адаптер для работы с фото
 *
 * Created by i.repkina on 07.11.2021.
 */
class BasePhotoAdapter(val callback: BasePhotoAdapterCallback) :
    RecyclerView.Adapter<BasePhotoAdapter.BasePhotoViewHolder>() {
    private var diffUtilCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.photoId == newItem.photoId
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.equals(newItem)
        }
    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    /**
     * Обновление списка фотографий в адаптере
     * @param photoList новый список фото
     */
    fun updateItems(photoList: List<Photo>, onUpdateItemsFunc: (() -> Unit)) {
        listDiffer.submitList(photoList, onUpdateItemsFunc)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasePhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_photo_item, parent, false)

        return BasePhotoViewHolder(itemView, callback)
    }

    override fun onBindViewHolder(holder: BasePhotoViewHolder, position: Int) {
        val currentList = listDiffer.currentList
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    class BasePhotoViewHolder(itemView: View, val callback: BasePhotoAdapterCallback) :
        RecyclerView.ViewHolder(itemView) {
        private val photoImageView = itemView.findViewById<ImageView>(R.id.imageViewPhoto)
        private val favoritePhotoBtn =
            itemView.findViewById<ImageView>(R.id.floatingButtonFavouritePhoto)

        fun bind(item: Photo) {
            Log.d(
                TAG,
                "bind() called with: item.selected = ${item.selected}, item.uri = ${item.uri}"
            )
            Glide.with(itemView.context)
                .load(Uri.parse(item.uri).path)
                .fitCenter()
                .into(photoImageView)

            favoritePhotoBtn.setImageLevel(item.selected.toInt())

            favoritePhotoBtn.setOnClickListener {
                if (item.photoId != null) {
                    if (item.selected) {
                        favoritePhotoBtn.setImageLevel(0)
                        callback.onChangedSelected(item.photoId)
                    } else {
                        favoritePhotoBtn.setImageLevel(1)
                        callback.onChangedSelected(item.photoId)
                    }
                }
            }
        }
    }

    /**
     * Интерфейс для вызова методов фрагмента
     */
    interface BasePhotoAdapterCallback {
        /**
         * Вызывает метод фрагмента и передает выбранное фото
         * @param photoId идентификатор фото
         */
        fun onChangedSelected(photoId: Long)
    }
}

// преобразует Boolean в Int
fun Boolean?.toInt(): Int {
    return if (this != null && this) 1 else 0
}


private const val TAG = "${Const.APP_TAG}.BasePhotoAdapter"
