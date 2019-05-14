package itis.ru.kpfu.join.presentation.util

import android.widget.EditText
import itis.ru.kpfu.join.presentation.util.textwatcher.TextWatcherAdapter

fun EditText.addTextChangedListener(
        isSkipInitialValue: Boolean = false,
        listener: (String) -> Unit
) {
    if (!isSkipInitialValue)
        listener(text.toString())
    addTextChangedListener(object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s.toString())
        }
    })
}