package com.irepka3.mygarden.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

/**
 * Таблица "Фотографии клумб"
 *
 * Created by i.repkina on 04.11.2021.
 */
@Entity(tableName = TABLE_NAME,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = FlowerbedEntity::class,
            parentColumns = arrayOf("flowerbedId"),
            childColumns = arrayOf("flowerbedId"),
            onDelete = CASCADE
        )
    ),
    indices = arrayOf(Index("flowerbedId"))
)
class FlowerbedPhotoEntity {
    // Идентификатор фото клумбы
    @PrimaryKey(autoGenerate = true)
    var flowerbedPhotoId: Long = 0
    // Идентификатор клумбы
    var flowerbedId: Long = 0
    // Uri фото клумбы
    var uri: String = ""
}

/**
 * Интерфейс для доступа к таблице Фотографии клумбы
 */
@Dao
interface FlowerbedPhotoEntityDao{
    // Получить список всех фото клумбы
    @Query("SELECT * FROM $TABLE_NAME WHERE flowerbedId = :flowerbedId")
    fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhotoEntity>?

    // Получить фото по идентификатору
    @Query("SELECT * FROM $TABLE_NAME WHERE flowerbedPhotoId = :flowerbedPhotoId")
    fun getById(flowerbedPhotoId: Long): FlowerbedPhotoEntity

    // Добавить фото клумбы
    @Insert
    fun insert(photo: FlowerbedPhotoEntity)

    // Удалить фото клумбы
    @Delete
    fun delete(photo: FlowerbedPhotoEntity)
}

private const val TABLE_NAME = "flowerbed_photo"