package com.irepka3.mygarden.data.database


import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.irepka3.mygarden.util.Const.APP_TAG


/**
 *
 * Таблица "Клумба"
 *
 * Created by i.repkina on 31.10.2021.
 */
@Entity(tableName = TABLE_NAME)
class FlowerbedEntity {
    // идентификатор клумбы
    @PrimaryKey(autoGenerate = true)
    var flowerbedId: Long = 0
    // название клумбы
    var name: String = ""
    // описание клумбы
    var description: String = ""
    // комментарий к клумбе
    var comment: String? = null
}

/**
 * Интерфейс для доступа к таблице "Клумба"
 */
@Dao
interface FlowerbedEntityDao{
    // Получить список всех клумб
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<FlowerbedEntity>?

    // Получить клумбу по ее идентификатору
    @Query("SELECT * FROM $TABLE_NAME WHERE flowerbedId = :flowerbedId")
    fun getById(flowerbedId: Long): FlowerbedEntity

    // Добавить клумбу
    @Insert
    fun insert(flowerbed: FlowerbedEntity)

    // Обновить данные клумбы
    @Update
    fun update(flowerbed: FlowerbedEntity)

    // Удалить клумбу
    @Delete
    fun delete(flowerbed: FlowerbedEntity)
}

private const val TABLE_NAME = "flowerbed"