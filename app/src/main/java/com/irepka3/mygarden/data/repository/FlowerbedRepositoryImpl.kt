package com.irepka3.mygarden.data.repository

import android.util.Log
import com.irepka3.mygarden.data.database.AppRoomDataBase
import com.irepka3.mygarden.data.database.FlowerbedEntity
import com.irepka3.mygarden.data.database.FlowerbedWithPhoto
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

class FlowerbedRepositoryImpl
@Inject constructor(private val database: AppRoomDataBase)
    : FlowerbedRepository {

    init {
        Log.d(TAG, "init called")
    }

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

    /**
     * Трансформирует модель доменного слоя [Flowerbed] в запись таблицы [FlowerbedEntity]
     */
    private fun Flowerbed.toEntity(): FlowerbedEntity {
        val flowerbedEntity = FlowerbedEntity()
        flowerbedEntity.flowerbedId = this.flowerbedId ?: 0
        flowerbedEntity.name = this.name
        flowerbedEntity.description = this.description
        flowerbedEntity.comment = this.comment
        return flowerbedEntity

    }

    /**
     * Трансформирует запись таблицы [FlowerbedEntity] в модель доменного слоя[Flowerbed]
     */
    private fun FlowerbedEntity.toDomain(): Flowerbed{
        return Flowerbed(this.flowerbedId, this.name, this.description, this.comment, null)
    }

    /**
     * Трансформирует запись таблицы [FlowerbedWithPhoto] в модель доменного слоя[Flowerbed]
     */
    private fun FlowerbedWithPhoto.toDomain(): Flowerbed{
        Log.d(TAG, "FlowerbedWithPhoto.toDomain() called uri = ${this.photoUri}")
        return Flowerbed(
            flowerbedId = this.flowerbed.flowerbedId,
            name = this.flowerbed.name,
            description = this.flowerbed.description,
            comment = this.flowerbed.comment,
            uri = this.photoUri
        )
    }
}

private const val  TAG = "${APP_TAG}.FlowerbedRepositoryImpl"