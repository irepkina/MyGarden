package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.mapper.toDomain
import com.irepka3.mygarden.data.mapper.toEntity
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.domain.repository.FlowerbedRepository
import com.irepka3.mygarden.util.Const.APP_TAG
import javax.inject.Inject


/**
 * Репозиторий для работы с клумбами
 * @param database база данных
 *
 * Created by i.repkina on 31.10.2021.
 */

class FlowerbedRepositoryImpl @Inject constructor(
    private val database: AppRoomDataBase
) : FlowerbedRepository {

    override fun getFlowerbedAll(): List<Flowerbed> {
        Log.d(TAG, "getFlowerBedAll() called")
        return database.flowerbedDao.getAll()?.map { it.toDomain() } ?: emptyList()
    }

    override fun getFlowerbed(flowerbedId: Long): Flowerbed {
        Log.d(TAG, "getFlowerBed() called with: id = $flowerbedId")
        return database.flowerbedDao.getById(flowerbedId).toDomain()
    }

    override fun insertFlowerbed(flowerbed: Flowerbed): Long {
        Log.d(TAG, "insertFlowerBed() called with: flowerBed = $flowerbed")
        return database.flowerbedDao.insert(flowerbed.toEntity()).also { flowerbedId ->
            Log.d(TAG, "insertFlowerBed() return flowerbedId = $flowerbedId")
        }
    }

    override fun updateFlowerbed(flowerbed: Flowerbed) {
        Log.d(TAG, "updateFlowerBed() called with: flowerBed = $flowerbed")
        database.flowerbedDao.update(flowerbed.toEntity())
    }

    override fun deleteFlowerbed(flowerbed: Flowerbed) {
        Log.d(TAG, "deleteFlowerBed() called with: flowerBed = $flowerbed")
        database.flowerbedDao.delete(flowerbed.toEntity())
    }
}


private const val TAG = "${APP_TAG}.FlowerbedRepositoryImpl"