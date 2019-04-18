package itis.ru.kpfu.join.presentation.util.pushprocessor

import android.content.Context

interface PushProcessor {

    fun processPush(context: Context, data: Map<String, String>)
}