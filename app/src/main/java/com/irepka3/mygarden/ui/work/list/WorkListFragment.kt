package com.irepka3.mygarden.ui.work.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irepka3.mygarden.R
import com.irepka3.mygarden.dagger
import com.irepka3.mygarden.databinding.FragmentWorklistBinding
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.ui.MainActivityIntf
import com.irepka3.mygarden.ui.util.recycleView.ItemTouchHelperFactory
import com.irepka3.mygarden.ui.work.model.WorkUIId
import com.irepka3.mygarden.util.Const

/**
 * Created by i.repkina on 10.11.2021.
 */
class WorkListFragment : Fragment(), WorkListAdapter.WorkListAdapterCallback {
    private lateinit var binding: FragmentWorklistBinding
    private val adapter = WorkListAdapter(this)

    private val viewModel by viewModels<WorkListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WorkListViewModel(
                    dagger().getWorkManagerInteractor()
                ) as T
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView() called")
        binding = FragmentWorklistBinding.inflate(inflater)

        val monthResourceArray = resources.getStringArray(R.array.period_month_array)

        // подписка на life-data view-модели
        viewModel.workLiveData.observe(viewLifecycleOwner) { list ->
            adapter.updateItems(list)
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result
        }
        viewModel.errorsLiveData.observe(viewLifecycleOwner) { error ->
            Log.e(TAG, "onCreateView() called with: error = ${error.message}", error)
            Toast.makeText(this.context, error.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.periodLiveData.observe(viewLifecycleOwner) { date ->
            binding.monthYear.setText("${monthResourceArray[date.monthValue]}, ${date.year}")
            viewModel.loadData()
        }

        viewModel.onCreateView()

        val itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        val drawable = this.resources.getDrawable(
            R.drawable.recycleview_divider_transparent,
            this.activity?.theme
        )
        itemDecorator.setDrawable(drawable)
        binding.workRecyclerView.addItemDecoration(itemDecorator)

        binding.workRecyclerView.layoutManager = LinearLayoutManager(
            this.context,
            RecyclerView.VERTICAL,
            false
        )
        binding.workRecyclerView.adapter = adapter

        binding.floatingButtonAddWork.setOnClickListener {
            (requireActivity() as MainActivityIntf).showWorkManagerFragment(
                WorkUIId(workId = null, repeatWorkId = null, planDate = null)
            )
        }

        val itemTouchHelper = ItemTouchHelperFactory.getItemTouchHelper { adapterPosition ->
            Log.d(TAG, "onCreateView() called with: adapterPosition = $adapterPosition")
            deleteWork(adapter.getItem(adapterPosition))
        }
        itemTouchHelper.attachToRecyclerView(binding.workRecyclerView)

        binding.buttonNextMonth.setOnClickListener {
            viewModel.onNextClick()
        }
        binding.buttonPreviousMonth.setOnClickListener {
            viewModel.onPreviousClick()
        }

        return binding.root
    }

    /**
     * Удаляет работу
     */
    private fun deleteWork(work: Work) {
        viewModel.onDelete(work)
    }

    override fun onWorkClick(workUIId: WorkUIId) {
        (this.activity as MainActivityIntf).showWorkManagerFragment(workUIId)
    }
}

private const val TAG = "${Const.APP_TAG}.WorkListFragment"