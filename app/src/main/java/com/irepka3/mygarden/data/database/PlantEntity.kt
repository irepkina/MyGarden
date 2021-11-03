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
import androidx.room.Update

/**
 * Таблицы "Растения"
 *
 * Created by i.repkina on 02.11.2021.
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
class PlantEntity {
    // Идентификатор растения
    @PrimaryKey(autoGenerate = true)
    var plantId: Long = 0
    // Идентификатор клумбы, на которой посажено растение
    var flowerbedId: Long = 0
    // Название растения
    var name: String = ""
    // Описание растения
    var description: String = ""
    // Количество посаженных в клумбе экземпляров растения
    var count: Int = 0
    // Дата посадки
    var plant_date: Long = 0
}

/**
 * Интерфейс для доступа к таблице Клумба
 */
@Dao
interface PlantEntityDao{
    // Получить список всех растений
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<PlantEntity>?

    // Получить растение по идентификатору
    @Query("SELECT * FROM $TABLE_NAME WHERE plantId = :plantId")
    fun getById(plantId: Long): PlantEntity

    // Получить все растения клумбы
    @Query("SELECT * FROM $TABLE_NAME WHERE flowerbedId = :flowerbedId")
    fun getPlantsByFlowerBedId(flowerbedId: Long): List<PlantEntity>?

    // Добавить растение
    @Insert
    fun insert(plant: PlantEntity)

    // Обновить данные растения
    @Update
    fun update(plant: PlantEntity)

    // Удалить растение
    @Delete
    fun delete(plant: PlantEntity)
}

private const val TABLE_NAME = "plant"