package com.irepka3.mygarden.domain.repository

import com.irepka3.mygarden.domain.model.Flowerbed

/**
 * Интерфейс доменного слоя для репозитория клумб.
 *
 * Created by i.repkina on 31.10.2021.
 */
interface FlowerbedRepository {
    // Получить список всех клумб
    fun getFlowerbedAll(): List<Flowerbed>
    // Добавить клумбу
    fun insertFlowerbed(flowerbed: Flowerbed)
    // Изменить данные клумбы
    fun updateFlowerbed(flowerbed: Flowerbed)
    // Удалить клумбу
    fun deleteFlowerbed(flowerbed: Flowerbed)
    // Получить клумбу по идентификатору
    fun getFlowerbed(flowerbedId: Long): Flowerbed?
}