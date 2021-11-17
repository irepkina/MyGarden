package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.Work

/**
 * Интерактор для работы со списком однократных работ
 *
 * Created by i.repkina on 11.11.2021.
 */
interface WorkInteractor {
    // Получение списка повторяющихся работ
    fun getAll(dateFrom: Long, dateTo: Long): List<Work>

    // Получение работы по идентификатору
    fun getById(workId: Long): Work

    // Добавление работы
    fun insertWork(work: Work): Long

    // Обновление данных работы
    fun updateWork(work: Work)

    // Удаление работы
    fun deleteWork(work: Work)
}