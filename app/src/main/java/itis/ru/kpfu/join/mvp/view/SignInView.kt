package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView


interface SignInView: MvpView {

    fun initClickListeners()

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun showResult(result: String)

    fun onCreateAccountClick()

    fun openSignUpFragment()

    fun signIn()
}