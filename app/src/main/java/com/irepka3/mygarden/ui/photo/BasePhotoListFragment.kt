package com.irepka3.mygarden.ui.flowerbed.photo.list

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.FragmentPhotoListBinding
import com.irepka3.mygarden.ui.photo.BasePhotoViewModel
import com.irepka3.mygarden.ui.photo.model.Photo
import com.irepka3.mygarden.ui.util.recycleView.GridSpacingItemDecorator
import com.irepka3.mygarden.util.Const.APP_TAG


/**
 * Фрагмент для отображения списка фотографий
 *
 * Created by i.repkina on 07.11.2021.
 */
abstract class BasePhotoListFragment : Fragment(),
    BasePhotoListAdapter.BasePhotoListAdapterCallback {
    private lateinit var binding: FragmentPhotoListBinding
    private val adapter = BasePhotoListAdapter(this)

    protected val viewModel: BasePhotoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return createViewModel() as T
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoListBinding.inflate(inflater)
        setFloatingButtonVisibility(EditMode.Default)

        readArguments()

        // подписка на life-data view-модели
        viewModel.photoListLiveData.observe(viewLifecycleOwner) { photoList ->
            adapter.updateItems(photoList)
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.e(TAG, "onCreateView() called with: error = ${error.message}", error)
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }

        val itemDecorator = GridSpacingItemDecorator(
            requireContext().resources,
            COLUMN_COUNT,
            R.dimen.grid_space,
            true
        )

        binding.photoRecyclerView.addItemDecoration(itemDecorator)

        this.binding.photoRecyclerView.layoutManager = GridLayoutManager(context, COLUMN_COUNT)
        adapter.adapterMode = EditMode.Default
        binding.photoRecyclerView.adapter = adapter

        binding.floatingButtonAddPhoto.setOnClickListener {
            adapter.adapterMode = EditMode.Default
            filesChooserContract.launch("image/*")
        }

        binding.floatingButtonDeletePhoto.setOnClickListener {
            deletePhoto(adapter.getPhotoToDelete())
        }
        return binding.root
    }

    // Создание контракта для выбора фото
    private val filesChooserContract =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            for (uri in uriList) {
                Log.d(TAG, "onActivityResult: uri = $uri")
                insertPhoto(uri)
            }
        }

    // Чтение аргументов
    abstract fun readArguments()
    abstract fun createViewModel(): BasePhotoViewModel

    // Добавляет фотографию
    private fun insertPhoto(externalUri: Uri) {
        viewModel.onInsert(externalUri)
    }

    // Удаляет фотографию
    private fun deletePhoto(photoList: List<Photo>) {
        Log.d(TAG, "deletePhoto() called with: photoList.size = ${photoList.size}")
        viewModel.onDelete(photoList)
        setFloatingButtonVisibility(EditMode.Default)
    }

    override fun photoClick(photoPosition: Int) {
        onPhotoClick(photoPosition)
    }

    override fun setEditMode(editMode: EditMode) {
        setFloatingButtonVisibility(editMode)
    }

    private fun setFloatingButtonVisibility(editMode: EditMode) {
        if (editMode == EditMode.DeleteMode) {
            binding.floatingButtonAddPhoto.isVisible = false
            binding.floatingButtonDeletePhoto.isVisible = true
        } else {
            binding.floatingButtonAddPhoto.isVisible = true
            binding.floatingButtonDeletePhoto.isVisible = false
        }
    }

    abstract fun onPhotoClick(photoPosition: Int)
}

private const val TAG = "${APP_TAG}.BasePhotoListFragment"

// количество столбцов в recycleview
private const val COLUMN_COUNT = 3
