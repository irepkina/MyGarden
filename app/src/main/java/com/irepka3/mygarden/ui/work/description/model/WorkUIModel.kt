package com.irepka3.mygarden.ui.work.description.model

import com.irepka3.mygarden.domain.model.Work
import com.irepka3.mygarden.domain.model.WorkStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Модель для вью редактирования работы
 * @property isOnceWork является ли работа однократно
 * @property workId идентификатор работы
 * @property repeatWorkId идентификатор повторяющейся работы
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
 * Created by i.repkina on 13.11.2021.
 */
data class WorkUIModel(
    var isOnceWork: Boolean,
    var workId: Long?,
    var repeatWorkId: Long?,
    var name: String?,
    var description: String?,
    var datePlan: String?,
    var dateDone: String?,
    var status: WorkStatus,
    var notificationDay: Int?,
    var notificationHour: Int?,
    var notificationMinute: Int?,
    var noNotification: Boolean
)

/**
 * Состояние полей на экране
 * @property workTitle название работы
 * @property workTypeUI тип работы [WorkTypeUI]
 * @property isEditMode режим редактирования
 * @property isRepeatSwitcherEnabled доступность переключателя Повторяющаяся работа
 * @property isDoneEnabled доступность кнопки Выполнить работу
 * @property isCancelEnabled доступность кнопки Отменить работу
 * @property isClearStatusEnabled доступность кнопки Очистить статус
 * @property isShowOriginRepeatEnabled открыть оригинальное повторяющееся событие
 * @property isReadOnly поля недоступны для редактирования
 * @property  isCommentReadonly поле Комментарий недоступно для редактирования
 */
data class WorkUIState(
    val workTitle: String,
    val workTypeUI: WorkTypeUI,
    val isEditMode: Boolean,
    val isRepeatSwitcherEnabled: Boolean,
    val isDoneEnabled: Boolean,
    val isCancelEnabled: Boolean,
    val isClearStatusEnabled: Boolean,
    val isShowOriginRepeatEnabled: Boolean,
    val isReadOnly: Boolean,
    val isCommentReadonly: Boolean
)

/**
 * Тип работы
 */
enum class WorkTypeUI {
    // новая работа (еще неопределенная)
    NEW_WORK,

    // однократная
    ONCE,

    // Оригинальная повторяющаяся
    ORIGIN_REPEAT,

    // сгенерированная на конкртеную дату повторяющаяся
    GENERATED_REPEAT,

    // сохраненная в базу повторяющаяся работа на конкретную дату
    SAVED_REPEAT
}

fun Work.toUIModel(): WorkUIModel {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val datePlan = if (this.datePlan !== null) {
        dateFormat.format(Date(this.datePlan))
    } else null

    val dateDone = if (this.dateDone !== null) {
        dateFormat.format(Date(this.dateDone))
    } else null

    return WorkUIModel(
        isOnceWork = this.repeatWork == null,
        workId = this.workId,
        repeatWorkId = this.repeatWork?.repeatWorkId,
        name = if (this.repeatWork != null) this.repeatWork.name else this.name,
        description = if (this.repeatWork != null && this.workId == null) this.repeatWork.description else this.description,
        datePlan = datePlan,
        dateDone = dateDone,
        status = this.status,
        notificationDay = if (this.repeatWork != null) this.repeatWork.notificationDay else this.notificationDay,
        notificationHour = if (this.repeatWork != null) this.repeatWork.notificationHour else this.notificationHour,
        notificationMinute = if (this.repeatWork != null) this.repeatWork.notificationMinute else this.notificationMinute,
        noNotification = if (this.repeatWork != null) this.repeatWork.noNotification else this.noNotification
    )
}

/**
 *
 */
fun Work.toWorkUIState(): WorkUIState {
    val workUiType = when {
        this.workId != null && this.repeatWork?.repeatWorkId == null -> WorkTypeUI.ONCE
        this.workId != null && this.workId == this.repeatWork?.repeatWorkId -> WorkTypeUI.ORIGIN_REPEAT
        this.workId == null && this.repeatWork?.repeatWorkId != null -> WorkTypeUI.GENERATED_REPEAT
        this.workId != null && this.repeatWork?.repeatWorkId != null -> WorkTypeUI.SAVED_REPEAT
        this.workId == null && this.repeatWork?.repeatWorkId == null -> WorkTypeUI.NEW_WORK
        else -> throw IllegalStateException("Invalid workUIState: work = $this")
    }

    return WorkUIState(
        workTitle = this.name,
        workTypeUI = workUiType,
        isEditMode = workUiType != WorkTypeUI.NEW_WORK,
        isRepeatSwitcherEnabled = workUiType == WorkTypeUI.NEW_WORK,
        isDoneEnabled = this.status == WorkStatus.Plan && workUiType != WorkTypeUI.NEW_WORK && workUiType != WorkTypeUI.ORIGIN_REPEAT,
        isCancelEnabled = this.status == WorkStatus.Plan && workUiType != WorkTypeUI.NEW_WORK,
        isClearStatusEnabled = this.status != WorkStatus.Plan && workUiType != WorkTypeUI.NEW_WORK,
        isShowOriginRepeatEnabled = workUiType == WorkTypeUI.GENERATED_REPEAT || workUiType == WorkTypeUI.SAVED_REPEAT,
        isReadOnly = workUiType == WorkTypeUI.GENERATED_REPEAT || workUiType == WorkTypeUI.SAVED_REPEAT,
        isCommentReadonly = false
    )
}
