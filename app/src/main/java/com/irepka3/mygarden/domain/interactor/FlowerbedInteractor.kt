package com.irepka3.mygarden.domain.interactor

import com.irepka3.mygarden.domain.model.Flowerbed

/**
 * Интерактор для работы со списком клумб
 *
 * Created by i.repkina on 01.11.2021.
 */
interface FlowerbedInteractor {
    // Получение списка клумб
    fun getFlowerbedAll(): List<Flowerbed>

    // Получение клумбы по идентификатору
    fun getFlowerbed(flowerbedId: Long): Flowerbed?

    // Добавление клумбы
    fun insertFlowerbed(flowerbed: Flowerbed): Long

    // Обновление данных клумбы
    fun updateFlowerbed(flowerbed: Flowerbed)

    // Удаление клумбы
    fun deleteFlowerbed(flowerbed: Flowerbed)
}