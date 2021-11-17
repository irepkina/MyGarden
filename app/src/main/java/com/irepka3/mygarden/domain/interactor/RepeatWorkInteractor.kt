package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.RepeatWork


/**
 * Интерактор для работы со списком повторяющихся работ
 *
 * Created by i.repkina on 10.11.2021.
 */
interface RepeatWorkInteractor {
    // Получение списка повторяющихся работ
    fun getAllActive(): List<RepeatWork>

    // Получение списка повторяющихся работ за месяц
    fun getAllActiveByMonth(month: Int): List<RepeatWork>

    // Получение повторяющейся работы по идентификатору
    fun getById(repeatWorkId: Long): RepeatWork

    // Добавление повторяющейся работы
    fun insertRepeatWork(repeatWork: RepeatWork): Long

    // Обновление данных повторяющейся работы
    fun updateRepeatWork(repeatWork: RepeatWork)

    // Удаление повторяющейся работы
    fun deleteRepeatWork(repeatWork: RepeatWork)
}