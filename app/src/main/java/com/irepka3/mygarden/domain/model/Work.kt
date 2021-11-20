package com.irepka3.mygarden.domain.model

/**
 * Модель работы доменного слоя
 * @property workId идентификатор работы
 * @property repeatWork повторяющайся работа [RepeatWork]
 * @property name наименование работы
 * @property description описание работы
 * @property datePlan плановая дата выполнения работы
 * @property dateDone дата выполнения работы
 * @property status статус работы [WorkStatus]
 * @property notificationDay количество дней, за которое нужно напомнить о работе
 * @property notificationHour время (часы), в которое нужно напомнить о выполнении работы
 * @property notificationMinute время (минуты), в которое нужно напомнить о выполнении работы
 * @property noNotification отключить напоминание о выполнении работы
 *
 * Created by i.repkina on 10.11.2021.
 */
data class Work(
    val workId: Long? = null,
    val repeatWork: RepeatWork? = null,
    val name: String = "",
    val description: String? = "",
    val datePlan: Long? = null,
    val dateDone: Long? = null,
    val status: WorkStatus = WorkStatus.Plan,
    val notificationDay: Int? = null,
    val notificationHour: Int? = null,
    val notificationMinute: Int? = null,
    val noNotification: Boolean = false
)

/**
 * Статус выполнения работы
 */
enum class WorkStatus(val value: Int) {
    Plan(0), Done(1), Cancel(2);

    companion object {
        fun fromValue(value: Int): WorkStatus {
            return when (value) {
                0 -> Plan
                1 -> Done
                2 -> Cancel
                else -> throw IllegalStateException("Invalid value = $value")
            }
        }
    }
}