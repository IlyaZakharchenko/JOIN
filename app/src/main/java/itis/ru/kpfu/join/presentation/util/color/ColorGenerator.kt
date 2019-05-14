package itis.ru.kpfu.join.presentation.util.color

import android.content.Context
import android.support.v4.content.ContextCompat
import itis.ru.kpfu.join.R

object ColorGenerator {
    fun getColor(context: Context): Int {
        val colors = listOf(
                R.color.colorAvatarOne,
                R.color.colorAvatarTwo,
                R.color.colorAvatarThree,
                R.color.colorAvatarFour,
                R.color.colorAvatarFive,
                R.color.colorAvatarSix,
                R.color.colorAvatarSeven,
                R.color.colorAvatarEight,
                R.color.colorAvatarNine,
                R.color.colorAvatarTen,
                R.color.colorAvatarEleven,
                R.color.colorAvatarTwelve
        )

        return ContextCompat.getColor(context, colors.shuffled()[0])
    }

}