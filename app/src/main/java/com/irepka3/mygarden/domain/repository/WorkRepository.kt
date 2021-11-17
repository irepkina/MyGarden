package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.Work

/**
 * Интерфейс доменного слоя для репозитория повторяющихся работ.
 *
 * Created by i.repkina on 10.11.2021.
 */
interface WorkRepository {
    // Получить список всех работ
    fun getAll(dateFrom: Long, dateTo: Long): List<Work>

    // Получить работу по идентификатору
    fun getById(workId: Long): Work?

    // Добавить работу
    fun insertWork(work: Work): Long

    // Изменить данные работы
    fun updateWork(work: Work)

    // Удалить работу
    fun deleteWork(work: Work)

}