package com.irepka3.mygarden.domain.util.date

import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.util.Calendar

/**
 * Тесты для класса [DateUtil]
 *
 * Created by i.repkina on 14.11.2021.
 */
class DateUtilTest {
    @Test
    fun toMillsTest() {
        val dateExpected = Calendar.getInstance()
        dateExpected.clear()
        dateExpected.set(2021, 0, 1)
        val dateActual = LocalDate.of(2021, 1, 1)

        Assert.assertEquals(dateExpected.time.time, dateActual.toMillis())
    }

    @Test
    fun getFirstMonthDayOfWeekTest() {
        val firstMondayExpected = LocalDate.of(2021, 12, 6)
        val firstMondayActual = getFirstMonthDayOfWeek(2021, 12, 1)
        Assert.assertEquals(firstMondayExpected, firstMondayActual)

        val firstSundayExpected = LocalDate.of(2021, 12, 5)
        val firstSundayActual = getFirstMonthDayOfWeek(2021, 12, 7)
        Assert.assertEquals(firstSundayExpected, firstSundayActual)

        val firstWednesdayExpected = LocalDate.of(2021, 12, 1)
        val firstWednesdayActual = getFirstMonthDayOfWeek(2021, 12, 3)
        Assert.assertEquals(firstWednesdayExpected, firstWednesdayActual)

        val firstSaturdayExpected = LocalDate.of(2022, 1, 3)
        val firstSaturdayActual = getFirstMonthDayOfWeek(2022, 1, 1)
        Assert.assertEquals(firstSaturdayExpected, firstSaturdayActual)
    }
}