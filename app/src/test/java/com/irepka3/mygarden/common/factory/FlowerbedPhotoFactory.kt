package com.irepka3.mygarden.common.factory

import com.irepka3.mygarden.data.database.FlowerbedPhotoEntity
import com.irepka3.mygarden.domain.model.FlowerbedPhoto

/**
 * Created by i.repkina on 22.11.2021.
 */
class FlowerbedPhotoFactory {
    companion object {
        fun createFlowerbedPhoto(flowerbedId: Long?, flowerbedPhotoId: Long?): FlowerbedPhoto {
            return FlowerbedPhoto(
                flowerbedId = flowerbedId ?: 0L,
                flowerbedPhotoId = flowerbedPhotoId,
                uri = "uri",
                selected = false,
                order = null
            )
        }

        fun createFlowerbedPhotoEntity(
            flowerbedPhotoId: Long?,
            flowerbedID: Long?
        ): FlowerbedPhotoEntity {
            return FlowerbedPhotoEntity(
                flowerbedPhotoId = flowerbedPhotoId ?: 0L,
                ownerFlowerbedId = flowerbedID ?: 0L,
                uri = "uri",
                selected = false,
                order = null
            )
        }
    }
}