package com.irepka3.mygarden.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update

/**
 * Таблица Периодическая работа
 *
 * Created by i.repkina on 10.11.2021.
 */
@Entity(tableName = RepeatWorkEntity.TABLE_NAME)
class RepeatWorkEntity {
    // идентификатор перодической работы
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_REPEAT_WORK_ID)
    var repeatWorkId: Long = 0

    // описание периодической работы
    @ColumnInfo(name = COLUMN_NAME)
    var name: String = ""

    // описание к периодической работе
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    var description: String? = ""

    // количество дней, за которое нужно напомнить о работе
    @ColumnInfo(name = COLUMN_NOTIFICATION_DAY)
    var notificationDay: Int? = null

    // время (часы), в которое нужно напомнить о выполнении работы
    @ColumnInfo(name = COLUMN_NOTIFICATION_HOUR)
    var notificationHour: Int? = null

    // время (минуты), в которое нужно напомнить о выполнении работы
    @ColumnInfo(name = COLUMN_NOTIFICATION_MINUTE)
    var notificationMinute: Int? = null

    // отключить напоминание о выполнении работы
    @ColumnInfo(name = COLUMN_NO_NOTIFICATION)
    var noNotification: Boolean = false

    // статус работы (0 - запланировано, 1 - выполнено, 2 - отменено)
    @ColumnInfo(name = COLUMN_STATUS)
    var status: Int = 0

    companion object {
        const val TABLE_NAME = "repeatWork"
        const val COLUMN_REPEAT_WORK_ID = "repeatWorkId"
        const val COLUMN_NAME = "repeatWorkName"
        const val COLUMN_DESCRIPTION = "repeatWorkDescription"
        const val COLUMN_NOTIFICATION_DAY = "repeatWorkNotificationDay"
        const val COLUMN_NOTIFICATION_HOUR = "repeatWorkNotificationHour"
        const val COLUMN_NOTIFICATION_MINUTE = "repeatWorkNotificationMinute"
        const val COLUMN_NO_NOTIFICATION = "repeatWorkNoNotification"
        const val COLUMN_STATUS = "repeatWorkStatus"
    }
}

class RepeatWorkWithScheduele {
    @Embedded
    lateinit var repeatWork: RepeatWorkEntity

    @Relation(
        parentColumn = RepeatWorkEntity.COLUMN_REPEAT_WORK_ID,
        entityColumn = ScheduleEntity.COLUMN_REPEAT_WORK_ID
    )
    lateinit var schedule: List<ScheduleEntity>
}

/**
 * Интерфейс для доступа к таблице "Периодическая работа"
 */
@Dao
interface RepeatWorkEntityDao {
    // Получить список всех активных работ с расписание
    @Transaction
    @Query(
        "SELECT * FROM ${RepeatWorkEntity.TABLE_NAME} " +
                "WHERE ${RepeatWorkEntity.COLUMN_STATUS} = 0"
    )
    fun getAllActive(): List<RepeatWorkWithScheduele>?

    // Получить периодическую работу по идентификатору
    @Transaction
    @Query(
        "SELECT * FROM ${RepeatWorkEntity.TABLE_NAME}  " +
                "WHERE ${RepeatWorkEntity.COLUMN_REPEAT_WORK_ID} = :repeatWorkId"
    )
    fun getById(repeatWorkId: Long): RepeatWorkWithScheduele?

    // Добавить периодическую работу
    @Insert
    fun insert(repeatWork: RepeatWorkEntity): Long

    // Обновить данные периодической работы
    @Update
    fun update(repeatWork: RepeatWorkEntity)

    // Удалить периодическую работу
    @Delete
    fun delete(repeatWork: RepeatWorkEntity)
}


