package com.irepka3.mygarden.domain.interactor

import android.util.Log
import com.irepka3.mygarden.domain.model.RepeatWork
import com.irepka3.mygarden.domain.model.Schedule
import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
import com.irepka3.mygarden.domain.util.date.getFirstMonthDayOfWeek
import com.irepka3.mygarden.domain.util.date.toMillis
import com.irepka3.mygarden.util.Const.APP_TAG
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

/**
 * Реализация интерактора для работы со списком однократных и повторяющихся работ
 *
 * Created by i.repkina on 11.11.2021.
 */
class WorkManagerInteractorImpl @Inject constructor(
    private val workInteractor: WorkInteractor,
    private val repeatWorkInteractor: RepeatWorkInteractor,
) : WorkManagerInteractor {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun getAll(year: Int, month: Int): List<Work> {
        val works = mutableListOf<Work>()
        val dateFrom = LocalDate.of(year, month, 1)
        val dateTo = dateFrom.withDayOfMonth(dateFrom.lengthOfMonth())
        // получаем разовые работы за период
        works.addAll(workInteractor.getAll(dateFrom.toMillis(), dateTo.toMillis()))

        // генерируем повторяющиеся события за период
        works.addAll(generateRepeatWorks(works, year, month))
        return works
    }

    override fun insert(isOnceWork: Boolean, work: Work): Long {
        return if (work.repeatWork !== null) {
            repeatWorkInteractor.insertRepeatWork(work.repeatWork)
        } else {
            workInteractor.insertWork(work)
        }
    }

    override fun delete(isOnceWork: Boolean, work: Work) {
        if (isOnceWork) {
            workInteractor.deleteWork(work)
        } else {
            if (work.repeatWork == null) {
                throw IllegalStateException("Invalid work.repeatWork = null in delete")
            }
            repeatWorkInteractor.deleteRepeatWork(work.repeatWork)
        }
    }

    override fun getById(workId: Long?, repeatWorkId: Long?): Work {
        return when {
            // одноркатная работа
            workId != null && repeatWorkId == null -> workInteractor.getById(workId)
            // сохраненная повторяющаяся работа на конкретную дату
            workId != null && repeatWorkId != null && workId != repeatWorkId -> {
                val repeatWork = repeatWorkInteractor.getById(repeatWorkId)
                val work = workInteractor.getById(workId)
                work.copy(repeatWork = repeatWork)
            }
            // оригинальная повторяющаяся работа
            repeatWorkId != null && workId == repeatWorkId -> {
                val repeatWork = repeatWorkInteractor.getById(repeatWorkId)
                Work(
                    workId = repeatWork.repeatWorkId,
                    repeatWork = repeatWork,
                    status = WorkStatus.Plan
                )
            }
            // сгенерированная повторяющаяся работа
            repeatWorkId != null && workId == null -> {
                val repeatWork = repeatWorkInteractor.getById(repeatWorkId)
                Work(workId = null, repeatWork = repeatWork, status = WorkStatus.Plan)
            }
            else -> throw IllegalStateException("Invalid arguments workId = null, repeatWorkId = null")
        }
    }

    override fun update(work: Work) {
        when {
            // сохраняем оригинальную повторяющуся работу
            work.repeatWork?.repeatWorkId != null && work.workId == work.repeatWork.repeatWorkId -> {
                repeatWorkInteractor.updateRepeatWork(work.repeatWork)
            }
            // сохраняем разовую работу
            work.workId != null -> {
                workInteractor.updateWork(work)
            }
            // сохраняем сгенерированную повторяющуся работу на конкретную дату
            work.workId == null && work.repeatWork?.repeatWorkId != null -> {
                workInteractor.insertWork(work)
            }
            else -> {
                throw IllegalStateException("Invalid work state, work = $work")
            }
        }
    }

    /**
     * Создает список плановых работ за указанный период
     * @param works список реальных работ [Work]
     * @param year год
     * @param month месяц
     */
    fun generateRepeatWorks(works: List<Work>, year: Int, month: Int): List<Work> {
        val resultWorkList = mutableListOf<Work>()
        // фильтруеи список реальных работ и создаем по ним индекс в виде set-а
        // (чтобы проверить, что сгенерированной работа не была сгенерирована ранее)
        val workIndexSet = works.filter { it.repeatWork != null }
            .map { RepeatWorkIndex(it.repeatWork?.repeatWorkId ?: 0L, it.datePlan ?: 0L) }
            .toSet()
        val repeatWorkList = repeatWorkInteractor.getAllActiveByMonth(month)
        repeatWorkList.forEach { repeatWork ->
            repeatWork.schedules.forEach { schedule ->
                val dateList = mutableListOf<Long>()
                val scheduleWeek = schedule.week
                if (scheduleWeek == null) {
                    for (week in 1..4) {
                        dateList.addAll(generateDataByWeek(year, month, week, schedule))
                    }
                } else {
                    dateList.addAll(generateDataByWeek(year, month, scheduleWeek, schedule))
                }
                dateList.filter {
                    !workIndexSet.contains(
                        RepeatWorkIndex(
                            repeatWork.repeatWorkId ?: 0L, it
                        )
                    )
                }
                    .forEach { date ->
                        resultWorkList.add(
                            Work(
                                workId = null,
                                repeatWork = repeatWork,
                                name = repeatWork.name,
                                description = repeatWork.description,
                                datePlan = date,
                                dateDone = null,
                                status = WorkStatus.Plan,
                                notificationDay = repeatWork.notificationDay,
                                notificationHour = repeatWork.notificationHour,
                                notificationMinute = repeatWork.notificationMinute,
                                noNotification = repeatWork.noNotification
                            )
                        )
                    }
            }
        }

        return resultWorkList
    }

    /**
     * Создает список дат по расписанию для указзанного года, месяца и номера недели
     * @param year год
     * @param month месяц
     * @param week номер недели
     * @param schedule расписание [Schedule]
     */
    fun generateDataByWeek(year: Int, month: Int, week: Int, schedule: Schedule): List<Long> {
        Log.d(
            TAG,
            "generateDataByWeek() called with: year = $year, month = $month, week = $week, schedule = $schedule"
        )
        if (week !in 1..4) throw IllegalStateException("generateDataByWeek(), invalid week = $week")
        val dateList = mutableListOf<Long>()
        if (schedule.monday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 1)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }
        if (schedule.tuesday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 2)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }
        if (schedule.wednesday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 3)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }
        if (schedule.thursday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 4)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }
        if (schedule.friday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 5)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }
        if (schedule.saturday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 6)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }
        if (schedule.sunday) {
            dateList.add(
                getFirstMonthDayOfWeek(year, month, 7)
                    .plusDays((week - 1) * 7L).toMillis()
            )
        }

        Log.d(TAG, "generateDataByWeek() dateList = ${dateList.map { dateFormat.format(it) }}")
        return dateList
    }

    fun RepeatWork.toWork(): Work {
        return Work(
            workId = null,
            repeatWork = this,
            name = this.name,
            description = this.description,
            datePlan = null,
            dateDone = null,
            status = WorkStatus.Plan,
            notificationDay = null,
            notificationHour = null,
            notificationMinute = null,
            noNotification = false
        )
    }

    private data class RepeatWorkIndex(private val id: Long, private val date: Long)
}

private const val TAG = "$APP_TAG.WorkManagerInteractorImpl"