package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView

interface ProfileEditView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onError(message : String)

}