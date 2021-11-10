package com.irepka3.mygarden.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.irepka3.mygarden.R
import com.irepka3.mygarden.databinding.ActivityMainBinding
import com.irepka3.mygarden.ui.flowerbed.FlowerbedFragment
import com.irepka3.mygarden.ui.flowerbed.list.FlowerbedListFragment
import com.irepka3.mygarden.ui.flowerbed.photo.photo.FragmentFlowerbedPhoto
import com.irepka3.mygarden.ui.flowerbed.plants.PlantFragment
import com.irepka3.mygarden.ui.flowerbed.plants.photo.photo.FragmentPlantPhoto
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

    //todo вывести в заголовок название клумбы, растения
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

    override fun showPlantFragment(flowerbedId: Long, plantId: Long?) {
        Log.d(TAG,"showPlantFragment() called with: flowerbedId = $flowerbedId, plantId = $plantId")
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.container,
                if (plantId !== null) PlantFragment.newInstanceUpdate(flowerbedId = flowerbedId, plantId = plantId) else
                    PlantFragment.newInstanceInsert(flowerbedId)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun showFlowerbedPhoto(flowerbedId: Long, photoPosition: Int){
        Log.d(TAG, "showFlowerbedPhoto() called")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FragmentFlowerbedPhoto.newInstance(flowerbedId, photoPosition), "FragmentFlowerbedPhoto")
            .addToBackStack(null)
            .commit()
    }

    override fun showPlantPhoto(flowerbedId: Long, plantId: Long, photoPosition: Int){
        Log.d(TAG, "showPlantPhoto() called")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FragmentPlantPhoto.newInstance(flowerbedId = flowerbedId, plantId = plantId, photoPosition), "FragmentPlantPhoto")
            .addToBackStack(null)
            .commit()
    }
}

interface MainActivityIntf {
    fun showFlowerbedList()
    fun showFlowerbedFragment(flowerbedId: Long?)
    fun showPlantFragment(flowerbedId: Long, plantId: Long?)
    fun showFlowerbedPhoto(flowerbedId: Long, photoPosition: Int)
    fun showPlantPhoto(flowerbedId: Long, plantId: Long, photoPosition: Int)

}

private const val  TAG = "$APP_TAG.MainActivity"