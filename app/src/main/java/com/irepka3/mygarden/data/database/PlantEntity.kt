package com.irepka3.mygarden.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

/**
 * Таблица "Растения"
 *
 * Created by i.repkina on 02.11.2021.
 */
@Entity(
    tableName = PlantEntity.TABLE_NAME,
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

data class PlantEntity(
    // Идентификатор растения
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var plantId: Long = 0,

    // Идентификатор клумбы, на которой посажено растение
    @ColumnInfo(name = COLUMN_FLOWERBED_ID)
    var flowerbedId: Long = 0,

    // Название растения
    @ColumnInfo(name = COLUMN_NAME)
    var name: String = "",

    // Описание растения
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    var description: String = "",

    // комментарий к растению
    @ColumnInfo(name = COLUMN_COMMENT)
    var comment: String? = null,

    // Количество посаженных в клумбе экземпляров растения
    @ColumnInfo(name = COLUMN_COUNT)
    var plantsCount: Int = 1,

    // Дата посадки
    @ColumnInfo(name = COLUMN_PLANT_DATE)
    var datePlant: Long? = null,

    //порядок отображения в списке
    @ColumnInfo(name = COLUMN_ORDER)
    var order: Int? = null
) {
    companion object {
        const val TABLE_NAME = "plant"
        const val COLUMN_ID = "plantId"
        const val COLUMN_FLOWERBED_ID = "flowerbedId"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_COMMENT = "comment"
        const val COLUMN_COUNT = "plantsCount"
        const val COLUMN_PLANT_DATE = "plant_date"
        const val COLUMN_ORDER = "number"
    }
}

/**
 * Растение с uri фото по умолчанию
 */
data class PlantWithPhoto(
    @Embedded
    var plant: PlantEntity,
    var photoUri: String? = null
)

/**
 * Интерфейс для доступа к таблице Клумба
 */
@Dao
interface PlantEntityDao {
    // Получить все растения клумбы
    @Query(
        "SELECT pl.*, ph.uri as photoUri FROM ${PlantEntity.TABLE_NAME} pl " +
                "LEFT JOIN ${PlantPhotoEntity.TABLE_NAME} ph " +
                "ON pl.${PlantEntity.COLUMN_ID} = ph.${PlantPhotoEntity.COLUMN_PLANT_ID}" +
                " AND ph.${PlantPhotoEntity.COLUMN_SELECTED} = 1 " +
                "WHERE pl.${PlantEntity.COLUMN_FLOWERBED_ID} = :flowerbedId"
    )
    fun getPlantsByFlowerBedId(flowerbedId: Long): List<PlantWithPhoto>?

    // Получить растение по идентификатору
    @Query("SELECT * FROM ${PlantEntity.TABLE_NAME} WHERE ${PlantEntity.COLUMN_ID} = :plantId")
    fun getById(plantId: Long): PlantEntity

    // Добавить растение
    @Insert
    fun insert(plant: PlantEntity): Long

    // Обновить данные растения
    @Update
    fun update(plant: PlantEntity)

    // Удалить растение
    @Delete
    fun delete(plant: PlantEntity)
}

