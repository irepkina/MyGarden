package com.irepka3.mygarden.domain.repository

import java.io.File

/**
 * Репозиторий возвращающий папки локального хранилища
 *
 * Created by i.repkina on 09.11.2021.
 */
interface DirRepository {
    // Возвращает корневую папку локального хранилища
    fun getCashDir(): File

    // Возвращает папку для фото клумб
    fun getFlowerbedDir(flowerbedId: Long): File

    // Возвращает папку для фото растений (вложена в папку клумбы)
    fun getPlantDir(flowerbedId: Long, plantId: Long): File
}