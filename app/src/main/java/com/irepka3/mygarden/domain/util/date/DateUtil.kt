package com.irepka3.mygarden.domain.util.date

import android.util.Log
import com.irepka3.mygarden.util.Const.APP_TAG
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale


/**
 * Функции для работы с датами
 *
 * Created by i.repkina on 14.11.2021.
 */


/**
 * Возвращает дату первого искомого дня недели в месяце
 * @param year год
 * @param month месяц
 * @param dayOfWeek номер дня недели (понедельник -1, воскресенье -7)
 */
fun getFirstMonthDayOfWeek(year: Int, month: Int, dayOfWeek: Int): LocalDate {
    Log.d(
        TAG,
        "getFirstMonthDayOfWeek() called with: month = $month, year = $year, dayOfWeek = $dayOfWeek"
    )
    val firstDate = LocalDate.of(year, month, 1)

    val firstDayOfWeek = firstDate.dayOfWeek.value

    val dayOfWeekDate = if (dayOfWeek < firstDayOfWeek) {
        firstDate.plusDays(7L - (firstDayOfWeek - dayOfWeek))
    } else {
        firstDate.plusDays(dayOfWeek.toLong() - firstDayOfWeek)
    }
    return dayOfWeekDate
}

// Перводит [LocalDate] в формат миллисекунд
fun LocalDate.toMillis(): Long {
    val mills = this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return mills
}

val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

// Переводит String в Long и возвращает ошибку
inline fun String?.toDate(onError: (Exception) -> Unit): Long? {
    return try {
        if (!this.isNullOrBlank()) dateFormat.parse(this)?.time else null
    } catch (e: Exception) {
        onError.invoke(e)
        null
    }
}

// Переводит String в Long
fun String?.toDate(): Long? {
    return try {
        if (!this.isNullOrBlank()) dateFormat.parse(this)?.time else null
    } catch (e: Exception) {
        null
    }
}

private const val TAG = "${APP_TAG}.DateUtil"