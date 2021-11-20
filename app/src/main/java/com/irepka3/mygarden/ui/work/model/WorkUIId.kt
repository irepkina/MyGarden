package com.irepka3.mygarden.ui.work.model

import java.io.Serializable

/**
 * Идентификатор работы (содержит ключевые поля для поиска работы)
 * Нужен для передачи идентификатора работы во фрагменты
 * @property workId идентификатор однократной работы
 * @property repeatWorkId идентификатор повторяющейся работы
 * @property planDate плановая дата
 *
 * Created by i.repkina on 16.11.2021.
 */

data class WorkUIId(
    val workId: Long?,
    val repeatWorkId: Long?,
    val planDate: Long?
) : Serializable