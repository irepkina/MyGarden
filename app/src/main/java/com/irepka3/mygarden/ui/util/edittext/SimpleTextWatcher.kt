package com.irepka3.mygarden.ui.util.edittext

import android.text.Editable

import android.text.TextWatcher

/**
 * Класс, упрощающий обработку события onTextChanged для EditText для от
 * @param onTextChangedFunc функция для обновления признака, что текст изменился, и для сбрасывания ошибки
 *
 * Created by i.repkina on 24.11.2021.
 */
class SimpleTextWatcher(private val onTextChangedFunc: () -> Unit) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChangedFunc()
    }

    override fun afterTextChanged(s: Editable?) {}
}