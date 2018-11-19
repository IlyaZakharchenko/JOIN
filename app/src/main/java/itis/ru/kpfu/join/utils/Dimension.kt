package itis.ru.kpfu.join.utils

import android.content.Context

fun toPx(dp: Int, context: Context): Int {
    val scale = context.resources.displayMetrics.density
    val px = (dp * scale + 0.5f).toInt()
    return px
}