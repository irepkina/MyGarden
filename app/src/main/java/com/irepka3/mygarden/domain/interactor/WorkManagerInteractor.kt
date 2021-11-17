package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.Work

/**
 * Интерактор для работы со списом повторяющихся работ и однократных
 *
 * Created by i.repkina on 11.11.2021.
 */
interface WorkManagerInteractor {
    // Получение списка работ
    fun getAll(year: Int, month: Int): List<Work>

    // Добавить работу
    fun insert(isOnceWork: Boolean, work: Work): Long

    // Удалить работу
    fun delete(isOnceWork: Boolean, work: Work)

    // Получить работу по идентификатору
    fun getById(workId: Long?, repeatWorkId: Long?): Work

    // Изменить данные работы
    fun update(work: Work)
}
