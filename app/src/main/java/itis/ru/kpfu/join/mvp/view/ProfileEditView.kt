package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView

interface PersonalProfileView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onError(message : String)

}