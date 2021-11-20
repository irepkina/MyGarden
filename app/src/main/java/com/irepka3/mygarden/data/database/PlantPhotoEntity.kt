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
 * Таблица "Фотографии растений"
 *
 * Created by i.repkina on 04.11.2021.
 */
@Entity(tableName = PlantPhotoEntity.TABLE_NAME,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = arrayOf(PlantEntity.COLUMN_ID),
            childColumns = arrayOf(PlantPhotoEntity.COLUMN_PLANT_ID),
            onDelete = CASCADE
        )
    ),
    indices = arrayOf(Index(PlantPhotoEntity.COLUMN_PLANT_ID))
)
class PlantPhotoEntity {
    // Идентификатор фото растения
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var plantPhotoId: Long = 0

    // Идентификатор растения
    @ColumnInfo(name = COLUMN_PLANT_ID)
    var plantId: Long = 0

    // Uri фото растения
    @ColumnInfo(name = COLUMN_URI)
    var uri: String = ""

    @ColumnInfo(name = COLUMN_SELECTED)
    //фото по умолчанию
    var selected: Boolean = false

    //порядок отображения в списке
    @ColumnInfo(name = COLUMN_ORDER)
    var order: Int? = null

    companion object {
        const val TABLE_NAME = "plant_photo"
        const val COLUMN_ID = "plantPhotoId"
        const val COLUMN_PLANT_ID = "plantId"
        const val COLUMN_URI = "uri"
        const val COLUMN_SELECTED = "selected"
        const val COLUMN_ORDER = "number"
    }
}

/**
 * Интерфейс для доступа к таблице Фотографии растений
 */
@Dao
interface PlantPhotoEntityDao{
    // Получить список всех фото растения
    @Query("SELECT * FROM ${PlantPhotoEntity.TABLE_NAME} WHERE ${PlantPhotoEntity.COLUMN_PLANT_ID} = :plantId")
    fun getAllByPlantId(plantId: Long): List<PlantPhotoEntity>?

    // Добавить фото клумбы
    @Insert
    fun insert(photo: PlantPhotoEntity)

    // Удалить фото клумбы
    @Delete
    fun delete(photo: List<PlantPhotoEntity>)

    // Установить фото по умолчанию для растения
    @Query("UPDATE  ${PlantPhotoEntity.TABLE_NAME} " +
            "SET selected = case when ${PlantPhotoEntity.COLUMN_ID} = :plantPhotoId then " +
            "case when ${PlantPhotoEntity.COLUMN_SELECTED} = 1 then 0 else 1 end else 0 end " +
            "WHERE ${PlantPhotoEntity.COLUMN_PLANT_ID} = :plantId ")
    fun updateSelectedPhoto(plantId: Long, plantPhotoId: Long)
}

