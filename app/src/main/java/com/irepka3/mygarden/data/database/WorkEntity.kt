package com.irepka3.mygarden.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update

/**
 * Таблица "Работа"
 *
 * Created by i.repkina on 11.11.2021.
 */
@Entity(
    tableName = WorkEntity.TABLE_NAME,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = RepeatWorkEntity::class,
            parentColumns = arrayOf(RepeatWorkEntity.COLUMN_REPEAT_WORK_ID),
            childColumns = arrayOf(WorkEntity.COLUMN_REPEAT_WORK_ID),
            onDelete = ForeignKey.SET_NULL
        )
    ),
    indices = arrayOf(
        Index(WorkEntity.COLUMN_REPEAT_WORK_ID)
    )
)
class WorkEntity {
    // идентификатор работы
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_WORK_ID)
    var workId: Long = 0

    // ссылка на периодическую работу
    @ColumnInfo(name = COLUMN_REPEAT_WORK_ID)
    var repeatWorkId: Long? = null

    // описание периодической работы
    @ColumnInfo(name = COLUMN_NAME)
    var name: String = ""

    // описание к периодической работе
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    var description: String? = ""

    // плановая дата работы
    @ColumnInfo(name = COLUMN_DATE_PLAN)
    var datePlan: Long? = 0

    // дата выполнения работы
    @ColumnInfo(name = COLUMN_DATE_DONE)
    var dateDone: Long? = null

    // статус работы (0 - запланировано, 1 - выполнено, 2 - отменено)
    @ColumnInfo(name = COLUMN_STATUS)
    var status: Int = 0

    // количество дней, за которое нужно напомнить о работе
    @ColumnInfo(name = COLUMN_NOTIFICATION_DAY)
    var notificationDay: Int? = null

    // время(часы), в которое нужно напомнить о выполнении работы
    @ColumnInfo(name = COLUMN_NOTIFICATION_HOUR)
    var notificationHour: Int? = null

    // время(минуты), в которое нужно напомнить о выполнении работы
    @ColumnInfo(name = COLUMN_NOTIFICATION_MINUTE)
    var notificationMinute: Int? = null

    // отключить напоминание о выполнении работы
    @ColumnInfo(name = COLUMN_NO_NOTIFICATION)
    var noNotification: Boolean = false

    companion object {
        const val TABLE_NAME = "work"
        const val COLUMN_WORK_ID = "workId"
        const val COLUMN_REPEAT_WORK_ID = "repeatWorkId"
        const val COLUMN_NAME = "workName"
        const val COLUMN_DESCRIPTION = "workDescription"
        const val COLUMN_DATE_PLAN = "datePlan"
        const val COLUMN_DATE_DONE = "dateDone"
        const val COLUMN_STATUS = "workStatus"
        const val COLUMN_NOTIFICATION_DAY = "workNotificationDay"
        const val COLUMN_NOTIFICATION_HOUR = "workNotificationHour"
        const val COLUMN_NOTIFICATION_MINUTE = "workNotificationMinute"
        const val COLUMN_NO_NOTIFICATION = "workNoNotification"
    }
}

class WorkWithRepeatWork {
    @Embedded
    var work: WorkEntity = WorkEntity()

    @Relation(
        parentColumn = WorkEntity.COLUMN_REPEAT_WORK_ID,
        entityColumn = RepeatWorkEntity.COLUMN_REPEAT_WORK_ID
    )
    var repeatWork: RepeatWorkEntity? = null

    @Relation(
        parentColumn = RepeatWorkEntity.COLUMN_REPEAT_WORK_ID,
        entityColumn = ScheduleEntity.COLUMN_REPEAT_WORK_ID
    )
    var schedule: List<ScheduleEntity>? = null
}


/**
 * Интерфейс для доступа к таблице "Работа"
 */
@Dao
interface WorkEntityDao {
    // Получить список всех работ
    @Transaction
    @Query(
        "SELECT * FROM ${WorkEntity.TABLE_NAME} " +
                "WHERE ${WorkEntity.COLUMN_DATE_PLAN} >= :dateFrom AND ${WorkEntity.COLUMN_DATE_PLAN} <= :dateTo "
    )
    fun getAll(dateFrom: Long, dateTo: Long): List<WorkWithRepeatWork>?

    // Получить список всех работ по расписанию
    @Transaction
    @Query(
        "SELECT * FROM ${WorkEntity.TABLE_NAME} " +
                "WHERE ${WorkEntity.COLUMN_WORK_ID} = :workId"
    )
    fun getById(workId: Long): WorkWithRepeatWork?

    // Добавить работу
    @Insert
    fun insert(work: WorkEntity): Long

    // Обновить данные работы
    @Update
    fun update(work: WorkEntity)

    // Удалить работу
    @Delete
    fun delete(work: WorkEntity)
}


