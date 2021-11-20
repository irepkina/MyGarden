package com.irepka3.mygarden.domain.util.date

import android.util.Log
import com.irepka3.mygarden.util.Const.APP_TAG
import java.time.LocalDate
import java.time.ZoneId


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
    //val mills =  this.toEpochDay() * 24 * 60 * 60 * 1000
    return mills
}

private const val TAG = "${APP_TAG}.DateUtil"