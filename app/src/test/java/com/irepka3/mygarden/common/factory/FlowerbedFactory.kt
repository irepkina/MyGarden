package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.FlowerbedEntity
import com.irepka3.mygarden.data.database.FlowerbedWithPhoto
import com.irepka3.mygarden.domain.model.Flowerbed
import com.irepka3.mygarden.domain.model.FlowerbedPhoto

/**
 * Создание экземляров клумб
 *
 * Created by i.repkina on 21.11.2021.
 */
class FlowerbedFactory {
    companion object {
        /**
         * Создает экземпляр класса [FlowerbedEntity]
         * @param flowerbedId идентификатор клумбы
         */
        fun createFlowerbedEntity(flowerbedId: Long?): FlowerbedEntity {
            val flowerbedEntity = FlowerbedEntity()
            flowerbedEntity.flowerbedId = flowerbedId ?: 0L
            flowerbedEntity.name = "name"
            flowerbedEntity.description = "description"
            flowerbedEntity.comment = "comment"
            flowerbedEntity.order = null
            return flowerbedEntity
        }

        /**
         * Создает экземпляр класса [FlowerbedWithPhoto]
         * @param flowerbedId идентификатор клумбы
         */
        fun createFlowerbedWithPhoto(flowerbedId: Long): FlowerbedWithPhoto {
            val flowerbedEntity = createFlowerbedEntity(flowerbedId)
            val flowerbedWithPhoto = FlowerbedWithPhoto()
            flowerbedWithPhoto.flowerbed = flowerbedEntity
            flowerbedWithPhoto.photoUri = "uri"
            return flowerbedWithPhoto
        }

        /**
         * Создает экземпляр класса [Flowerbed]
         * @param flowerbedId идентифкатор клумбы
         */
        fun createFlowerbed(flowerbedId: Long?): Flowerbed {
            return Flowerbed(
                flowerbedId = flowerbedId,
                name = "name",
                description = "description",
                comment = "comment",
                uri = "uri",
                order = null
            )
        }

        /**
         * Создает экземпляр класса [Flowerbed] с пустым Uri
         * @param flowerbedId идентификатор клумбы
         */
        fun createFlowerbedEmptyUri(flowerbedId: Long?): Flowerbed {
            return Flowerbed(
                flowerbedId = flowerbedId,
                name = "name",
                description = "description",
                comment = "comment",
                uri = null,
                order = null
            )
        }

        /**
         * Создает экземпляр класса [Flowerbed] с пустым полями
         */
        fun createFlowerbedEmpty(): Flowerbed {
            return Flowerbed()
        }

        /**
         * Создает экземпляр класса [FlowerbedPhoto]
         * @param flowerbedId  идентификатор клумбы
         * @param flowerbedPhotoId идентификатор фото клумбы
         */
        fun createFlowerbedPhoto(flowerbedId: Long?, flowerbedPhotoId: Long?): FlowerbedPhoto {
            return FlowerbedPhoto(
                flowerbedId = flowerbedId ?: 0L,
                flowerbedPhotoId = flowerbedPhotoId,
                uri = "uri",
                selected = false,
                order = null
            )
        }

    }
}