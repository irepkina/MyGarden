package com.irepka3.mygarden.ui.photo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.databinding.FragmentPhotoBinding
import com.irepka3.mygarden.util.Const


/**
 * Фрагмент для отображения фотографии
 *
 * Created by i.repkina on 07.11.2021.
 */
abstract class BasePhotoFragment : Fragment(), BasePhotoAdapter.BasePhotoAdapterCallback {
    private lateinit var binding: FragmentPhotoBinding

    protected var caption = ""
    private val adapter = BasePhotoAdapter(this)

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
        binding = FragmentPhotoBinding.inflate(inflater)

        readArguments()

        // подписка на life-data view-модели
        viewModel.photoListLiveData.observe(viewLifecycleOwner) { photoList ->
            val isFirstLoad = adapter.itemCount == 0
            adapter.updateItems(photoList) {
                if (isFirstLoad) {
                    binding.photoRecyclerView.scrollToPosition(scrollPosition())
                }
            }
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.e(TAG, "onCreateView() called with: error = ${error.message}", error)
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }

        this.binding.photoRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        binding.photoRecyclerView.adapter = adapter

        Log.d(TAG, "onCreateView(),scrollPosition() = ${scrollPosition()}")

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.photoRecyclerView)

        initToolBar()

        return binding.root
    }

    private fun initToolBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            val actionBar = supportActionBar
            actionBar?.title = caption

            actionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
            actionBar?.setDisplayShowHomeEnabled(supportFragmentManager.backStackEntryCount > 0)

            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onChangedSelected(photoId: Long) {
        Log.d(TAG, "onChangedSelected() called with: photoId = $photoId")
        viewModel.onSelected(photoId)
    }

    // Чтение аргументов
    abstract fun readArguments()
    abstract fun createViewModel(): BasePhotoViewModel
    abstract fun scrollPosition(): Int
}


private const val TAG = "${Const.APP_TAG}.BasePhotoFragment"