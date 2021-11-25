package com.irepka3.mygarden.data.database


import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update


/**
 *
 * Таблица "Клумба"
 *
 * Created by i.repkina on 31.10.2021.
 */
@Entity(tableName = FlowerbedEntity.TABLE_NAME)
data class FlowerbedEntity(
    // идентификатор клумбы
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_FLOWERBED_ID)
    var flowerbedId: Long = 0,

    // название клумбы
    @ColumnInfo(name = COLUMN_NAME)
    var name: String = "",

    // описание клумбы
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    var description: String = "",

    // комментарий к клумбе
    @ColumnInfo(name = COLUMN_COMMENT)
    var comment: String? = null,

    //порядок отображения в списке
    @ColumnInfo(name = COLUMN_ORDER)
    var order: Int? = null
) {
    companion object {
        const val TABLE_NAME = "flowerbed"
        const val COLUMN_FLOWERBED_ID = "flowerbedId"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_COMMENT = "comment"
        const val COLUMN_ORDER = "number"
    }
}

/**
 * Клума с uri фото по умолчанию
 */
class FlowerbedWithPhoto {
    @Embedded
    lateinit var flowerbed: FlowerbedEntity
    var photoUri: String? = null
}

/**
 * Интерфейс для доступа к таблице "Клумба"
 */
@Dao
interface FlowerbedEntityDao {
    // Получить список всех клумб
    @Query(
        "SELECT fb.*, ph.uri as photoUri FROM ${FlowerbedEntity.TABLE_NAME} fb " +
                "LEFT JOIN ${FlowerbedPhotoEntity.TABLE_NAME} ph " +
                "ON fb.${FlowerbedEntity.COLUMN_FLOWERBED_ID} = ph.${FlowerbedPhotoEntity.COLUMN_FLOWERBED_ID} " +
                "AND ph.${FlowerbedPhotoEntity.COLUMN_SELECTED} = 1 "
    )
    fun getAll(): List<FlowerbedWithPhoto>?

    // Получить клумбу по ее идентификатору
    @Query(
        "SELECT * FROM ${FlowerbedEntity.TABLE_NAME} " +
                "WHERE ${FlowerbedEntity.COLUMN_FLOWERBED_ID} = :flowerbedId"
    )
    fun getById(flowerbedId: Long): FlowerbedEntity

    // Добавить клумбу
    @Insert
    fun insert(flowerbed: FlowerbedEntity): Long

    // Обновить данные клумбы
    @Update
    fun update(flowerbed: FlowerbedEntity)

    // Удалить клумбу
    @Delete
    fun delete(flowerbed: FlowerbedEntity)
}


