package com.irepka3.mygarden.domain.model

/**
 * Модель периодической работы доменного слоя
 * @property repeatWorkId идентификатор периодической работы
 * @property name описание периодической работы
 * @property description комментарий для периодической работы
 * @property notificationDay количество дней, за которое нужно напомнить о работе
 * @property notificationHour время (часы), в которое нужно напомнить о выполнении работы
 * @property notificationMinute время (минуты), в которое нужно напомнить о выполнении работы
 * @property noNotification отключить напоминание о выполнении работы
 * @property schedules спискок расписаний [Schedule]
 *
 * Created by i.repkina on 10.11.2021.
 */
data class RepeatWork(
    val repeatWorkId: Long? = null,
    val name: String = "",
    val description: String? = "",
    val notificationDay: Int? = null,
    val notificationHour: Int? = null,
    val notificationMinute: Int? = null,
    val noNotification: Boolean = false,
    val status: WorkStatus = WorkStatus.Plan,
    val schedules: List<Schedule> = emptyList()
)
