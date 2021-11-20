package com.irepka3.mygarden.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

/**
 * Таблица "Расписание работ"
 *
 * Created by i.repkina on 10.11.2021.
 */
@Entity(
    tableName = ScheduleEntity.TABLE_NAME,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = RepeatWorkEntity::class,
            parentColumns = arrayOf(RepeatWorkEntity.COLUMN_REPEAT_WORK_ID),
            childColumns = arrayOf(ScheduleEntity.COLUMN_REPEAT_WORK_ID),
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = arrayOf(
        Index(ScheduleEntity.COLUMN_REPEAT_WORK_ID)
    )
)
class ScheduleEntity {
    // идентификатор расписания работ
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_SCHEDULE_ID)
    var scheduleId: Long = 0

    // идентификатор периодической работы
    @ColumnInfo(name = COLUMN_REPEAT_WORK_ID)
    var repeatWorkId: Long = 0

    // месяц выполнения работы
    @ColumnInfo(name = COLUMN_MONTH)
    var month: Int? = null

    // неделя выполнения работы
    @ColumnInfo(name = COLUMN_WEEK)
    var week: Int? = null

    // день недели выполнения работы
    @ColumnInfo(name = COLUMN_MONDAY)
    // понедельник
    var monday: Boolean = false

    // вторник
    @ColumnInfo(name = COLUMN_TUESDAY)
    var tuesday: Boolean = false

    // среда
    @ColumnInfo(name = COLUMN_WEDNESDAY)
    var wednesday: Boolean = false

    // четверг
    @ColumnInfo(name = COLUMN_THURSDAY)
    var thursday: Boolean = false

    // пятница
    @ColumnInfo(name = COLUMN_FRIDAY)
    var friday: Boolean = false

    // суббота
    @ColumnInfo(name = COLUMN_SATURDAY)
    var saturday: Boolean = false

    // воскресенье
    @ColumnInfo(name = COLUMN_SUNDAY)
    var sunday: Boolean = false

    companion object {
        const val TABLE_NAME = "schedule"
        const val COLUMN_SCHEDULE_ID = "scheduleId"
        const val COLUMN_REPEAT_WORK_ID = "ownerRepeatWorkId"
        const val COLUMN_MONTH = "month"
        const val COLUMN_WEEK = "week"
        const val COLUMN_MONDAY = "monday"
        const val COLUMN_TUESDAY = "tuesday"
        const val COLUMN_WEDNESDAY = "wednesday"
        const val COLUMN_THURSDAY = "thursday"
        const val COLUMN_FRIDAY = "friday"
        const val COLUMN_SATURDAY = "saturday"
        const val COLUMN_SUNDAY = "sunday"
    }
}

/**
 * Интерфейс для доступа к таблице "Расписание работ"
 */
@Dao
interface ScheduleEntityDao {
    // Получить список всех периодических работ повторяющейся работы
    @Query(
        "SELECT * FROM ${ScheduleEntity.TABLE_NAME} sch " +
                "WHERE sch.${ScheduleEntity.COLUMN_REPEAT_WORK_ID} =  :repeatWorkId"
    )
    fun getByRepeatWorkId(repeatWorkId: Long): List<ScheduleEntity>?

    // Получить расписание по ID
    @Query(
        "SELECT * FROM ${ScheduleEntity.TABLE_NAME} " +
                "WHERE ${ScheduleEntity.COLUMN_SCHEDULE_ID} = :scheduleId"
    )
    fun getById(scheduleId: Long): ScheduleEntity

    // Добавить периодическую работу
    @Insert
    fun insert(schedules: List<ScheduleEntity>): List<Long>

    // Обновить данные периодической работы
    @Update
    fun update(schedules: List<ScheduleEntity>)

    // Удалить периодическую работу
    @Delete
    fun delete(schedule: ScheduleEntity)
}


