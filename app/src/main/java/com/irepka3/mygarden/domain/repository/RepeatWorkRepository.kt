package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.RepeatWork

/**
 * Интерфейс доменного слоя для репозитория повторяющихся работ.
 *
 * Created by i.repkina on 10.11.2021.
 */
interface RepeatWorkRepository {
    // Получить список всех повторяющихся работ
    fun getAllActive(): List<RepeatWork>

    // Получить список повторяющихся работ за месяц
    fun getAllActiveByMonth(month: Int): List<RepeatWork>

    // Получить повторяющуюся работу по идентификатору
    fun getById(repeatWorkId: Long): RepeatWork?

    // Добавить повторяющуся работу
    fun insertRepeatWork(repeatWork: RepeatWork): Long

    // Изменить данные повторяющейся работы
    fun updateRepeatWork(repeatWork: RepeatWork)

    // Удалить повторяющуся работу
    fun deleteRepeatWork(repeatWork: RepeatWork)
}