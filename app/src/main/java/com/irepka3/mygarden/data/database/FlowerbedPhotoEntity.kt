package com.irepka3.mygarden.data.database

import androidx.room.ColumnInfo
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
@Entity(tableName = FlowerbedPhotoEntity.TABLE_NAME,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = FlowerbedEntity::class,
            parentColumns = arrayOf(FlowerbedEntity.COLUMN_FLOWERBED_ID),
            childColumns = arrayOf(FlowerbedPhotoEntity.COLUMN_FLOWERBED_ID),
            onDelete = CASCADE
        )
    ),
    indices = arrayOf(Index(FlowerbedPhotoEntity.COLUMN_FLOWERBED_ID))
)
class FlowerbedPhotoEntity {
    // Идентификатор фото клумбы
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var flowerbedPhotoId: Long = 0

    // Идентификатор клумбы
    @ColumnInfo(name = COLUMN_FLOWERBED_ID)
    var ownerFlowerbedId: Long = 0

    // Uri фото клумбы
    @ColumnInfo(name = COLUMN_URI)
    var uri: String = ""

    // Фото клумбы по умолчанию
    @ColumnInfo(name = COLUMN_SELECTED)
    var selected: Boolean = false

    //порядок отображения в списке
    @ColumnInfo(name = COLUMN_ORDER)
    var order: Int? = null

    companion object {
        const val TABLE_NAME = "flowerbed_photo"
        const val COLUMN_ID = "flowerbedPhotoId"
        const val COLUMN_FLOWERBED_ID = "ownerFlowerbedId"
        const val COLUMN_URI = "uri"
        const val COLUMN_SELECTED = "selected"
        const val COLUMN_ORDER = "number"
    }
}

/**
 * Интерфейс для доступа к таблице Фотографии клумбы
 */
@Dao
interface FlowerbedPhotoEntityDao{
    // Получить список всех фото клумбы
    @Query("SELECT * FROM ${FlowerbedPhotoEntity.TABLE_NAME} WHERE ${FlowerbedPhotoEntity.COLUMN_FLOWERBED_ID} = :flowerbedId")
    fun getAllByFlowerbedId(flowerbedId: Long): List<FlowerbedPhotoEntity>?

    // Добавить фото клумбы
    @Insert
    fun insert(photo: FlowerbedPhotoEntity)

    // Удалить фото клумбы
    @Delete
    fun delete(photo: List<FlowerbedPhotoEntity>)

    // Установить фото по умолчанию для клумбы
    @Query("UPDATE  ${FlowerbedPhotoEntity.TABLE_NAME} " +
            "SET selected = case when ${FlowerbedPhotoEntity.COLUMN_ID} = :flowerbedPhotoId then " +
            "case when ${FlowerbedPhotoEntity.COLUMN_SELECTED} = 1 then 0 else 1 end else 0 end " +
            "WHERE ${FlowerbedPhotoEntity.COLUMN_FLOWERBED_ID} = :flowerbedId ")
    fun updateSelectedPhoto(flowerbedId: Long, flowerbedPhotoId: Long)
}

