package com.irepka3.mygarden.ui.work.description.model

import com.irepka3.mygarden.domain.model.Schedule
import java.io.Serializable

/**
 * Модель для вью настройки расписания
 * @property schedule расписание [Schedule]
 * @property number - орядковый номер расписания
 *
 * Created by i.repkina on 11.11.2021.
 */
data class ScheduleUIModel(
    var schedule: Schedule,
    var number: Int?
) : Serializable

fun Schedule.toUIModel(number: Int): ScheduleUIModel {
    return ScheduleUIModel(
        schedule = this,
        number = number
    )
}