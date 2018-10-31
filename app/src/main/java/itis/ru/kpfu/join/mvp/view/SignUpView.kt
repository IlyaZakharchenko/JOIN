package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView

interface SignUpView: MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun openMainFragment()

    fun onSignUpClick()
}