package com.irepka3.mygarden.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.ActivityMainBinding
import com.irepka3.mygarden.ui.flowerbed.FlowerbedFragment
import com.irepka3.mygarden.ui.list.FlowerbedListFragment
import com.irepka3.mygarden.util.Const.APP_TAG

class MainActivity : AppCompatActivity(), MainActivityIntf {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()
        showFlowerbedList()
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun showFlowerbedList(){
        Log.d(TAG, "showFlowerbedList() called")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FlowerbedListFragment(), "FlowerbedListFragment")
            .commit()
    }

    //todo вывести в заголовок название клумбы
    fun setCaption(flowerbed: String) {
            val actionBar = supportActionBar
            actionBar?.title = flowerbed
    }

    override fun showFlowerbedFragment(flowerbedId: Long?) {
        Log.d(TAG, "showFlowerbedFragment() called with: flowerbedId = $flowerbedId")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container,
                if (flowerbedId !== null) FlowerbedFragment.newInstanceUpdate(flowerbedId) else
                    FlowerbedFragment.newInstanceInsert()
            )
            .addToBackStack(null)
            .commit()
    }

    override fun showPlantFragment(plantId: Long?) {
        TODO("Not yet implemented")
    }
}

interface MainActivityIntf {
    fun showFlowerbedList()
    fun showFlowerbedFragment(flowerbedId: Long?)
    fun showPlantFragment(plantId: Long?)
}

private const val  TAG = "$APP_TAG.MainActivity"