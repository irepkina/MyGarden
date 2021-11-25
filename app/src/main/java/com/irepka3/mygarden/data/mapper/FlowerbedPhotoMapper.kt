package com.irepka3.mygarden.data.mapper

import com.irepka3.mygarden.data.database.FlowerbedPhotoEntity
import com.irepka3.mygarden.domain.model.FlowerbedPhoto

/**
 * Трансформирует запись таблицы [FlowerbedPhotoEntity] в модель доменного слоя[FlowerbedPhoto]
 */
fun FlowerbedPhotoEntity.toDomain(): FlowerbedPhoto {
    return FlowerbedPhoto(
        flowerbedId = this.ownerFlowerbedId,
        flowerbedPhotoId = this.flowerbedPhotoId,
        uri = this.uri,
        selected = this.selected,
        order = this.order
    )
}

/**
 * Трансформирует модель доменного слоя [FlowerbedPhoto] в запись таблицы [FlowerbedPhotoEntity]
 */
fun FlowerbedPhoto.toEntity(): FlowerbedPhotoEntity {
    val flowerbedPhotoEntity = FlowerbedPhotoEntity()
    flowerbedPhotoEntity.flowerbedPhotoId = this.flowerbedPhotoId ?: 0L
    flowerbedPhotoEntity.ownerFlowerbedId = this.flowerbedId
    flowerbedPhotoEntity.uri = this.uri
    flowerbedPhotoEntity.selected = this.selected
    flowerbedPhotoEntity.order = this.order
    return flowerbedPhotoEntity
}