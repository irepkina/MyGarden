package com.irepka3.mygarden.domain.model

import java.io.Serializable

/**
 * Модель расписание работ доменного слоя
 * @property scheduleId дентификатор расписания
 * @property repeatWorkId - идентификатор повторяющейся работы
 * @property month - месяц выполнения работы
 * @property week - неделя выполнения работы
 * @property monday - понедельник
 * @property tuesday - вторник
 * @property wednesday - среда
 * @property thursday - четверг
 * @property friday - пятница
 * @property saturday - суббота
 * @property sunday - воскресенье
 *
 * Created by i.repkina on 11.11.2021.
 */
data class Schedule(
    var scheduleId: Long? = null,
    var repeatWorkId: Long? = null,
    var month: Int? = null,
    var week: Int? = null,
    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
    var sunday: Boolean = false
) : Serializable
