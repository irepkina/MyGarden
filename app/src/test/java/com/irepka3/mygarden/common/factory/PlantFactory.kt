package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.PlantEntity
import com.irepka3.mygarden.data.database.PlantWithPhoto
import com.irepka3.mygarden.domain.model.Plant

/**
 * Создание экземпляров растений
 *
 * Created by i.repkina on 21.11.2021.
 */
class PlantFactory {
    companion object {
        /**
         * Создает экземпляр класса [Plant]
         * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun createPlant(flowerbedId: Long?, plantId: Long?): Plant {
            return Plant(
                flowerbedId = flowerbedId ?: 0L,
                plantId = plantId,
                name = "name",
                description = "description",
                comment = "comment",
                count = 1,
                datePlant = null,
                uri = "uri",
                order = null
            )
        }

        /**
         * Создает экземпляр класса [Plant] с пустым uri
         * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun createPlantEmptyUri(flowerbedId: Long?, plantId: Long?): Plant {
            return Plant(
                flowerbedId = flowerbedId ?: 0L,
                plantId = plantId,
                name = "name",
                description = "description",
                comment = "comment",
                count = 1,
                datePlant = null,
                uri = null,
                order = null
            )
        }

        /**
         * Создает экземпляр класса [Plant] с пустыми параметрами
         * @param flowerbedId идентификатор клумбы
         */
        fun createPlantEmpty(flowerbedId: Long?): Plant {
            return Plant(flowerbedId = flowerbedId ?: 0L)
        }

        /**
         * Создает экземпляр класса [PlantEntity]
         * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun createPlantEntity(flowerbedId: Long?, plantId: Long?): PlantEntity {
            return PlantEntity(
                flowerbedId = flowerbedId ?: 0L,
                plantId = plantId ?: 0L,
                name = "name",
                description = "description",
                comment = "comment",
                plantsCount = 1,
                datePlant = null,
                order = null
            )
        }

        /**
         * Создает экземпляр класса [PlantWithPhoto]
         * @param flowerbedId идентификатор клумбы
         * @param plantId идентификатор растения
         */
        fun createPlantWithPhoto(flowerbedId: Long?, plantId: Long?): PlantWithPhoto {
            val plantEntity = PlantEntity(
                flowerbedId = flowerbedId ?: 0L,
                plantId = plantId ?: 0L,
                name = "name",
                description = "description",
                comment = "comment",
                plantsCount = 1,
                datePlant = null,
                order = null
            )
            return PlantWithPhoto(plant = plantEntity, photoUri = "uri")
        }


    }
}