package com.irepka3.mygarden.ui.mainpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.FragmentMainPageBinding
import com.irepka3.mygarden.util.Const


/**
 * Created by i.repkina on 10.11.2021.
 */
class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var myViewPager2: ViewPager2
    private lateinit var myAdapter: MainPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(
            TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState"
        )
        binding = FragmentMainPageBinding.inflate(inflater)

        myViewPager2 = binding.pager
        myAdapter = MainPageAdapter(this.childFragmentManager, this.lifecycle)
        myViewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        myViewPager2.adapter = myAdapter
        myViewPager2.setPageTransformer(MarginPageTransformer(MARGIN))

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_flowerbedlist_text)
                1 -> tab.text = getString(R.string.tab_repeat_work_text)
            }
        }.attach()

        initToolBar()
        return binding.root
    }

    private fun initToolBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            val actionBar = supportActionBar

            actionBar?.setDisplayHomeAsUpEnabled(false)
            actionBar?.setDisplayShowHomeEnabled(false)

            binding.collapsingToolbar.title = context?.getString(R.string.app_name)

            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

}

private const val TAG = "${Const.APP_TAG}.MainPageFragment"
private const val MARGIN = 1500