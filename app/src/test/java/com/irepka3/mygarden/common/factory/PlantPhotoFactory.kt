package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.PlantPhotoEntity
import com.irepka3.mygarden.domain.model.PlantPhoto

/**
 * Классдля создания экземпляров фото растений
 * Created by i.repkina on 22.11.2021.
 */
class PlantPhotoFactory {
    companion object {
        fun createPlantPhoto(plantId: Long?, plantPhotoId: Long?): PlantPhoto {
            return PlantPhoto(
                plantId = plantId ?: 0L,
                plantPhotoId = plantPhotoId,
                uri = "uri",
                selected = false,
                order = null
            )
        }

        fun createPlantPhotoEntity(plantID: Long?, plantPhotoId: Long?): PlantPhotoEntity {
            return PlantPhotoEntity(
                plantId = plantID ?: 0L,
                plantPhotoId = plantPhotoId ?: 0L,
                uri = "uri",
                selected = false,
                order = null
            )
        }
    }
}