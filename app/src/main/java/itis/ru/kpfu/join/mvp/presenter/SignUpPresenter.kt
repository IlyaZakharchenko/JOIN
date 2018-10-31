package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.mvp.view.SignUpView

@InjectViewState
class SignUpPresenter: MvpPresenter<SignUpView>() {

    private fun checkUsernameUniqueness(){
        //TODO check username uniqueness
    }

    private fun checkEmailUniqueness() {
        //TODO check email uniqueness
    }

    private fun sendDataToServer() {
        //TODO send account data to server
    }

    private fun getDataFromServer() {
        //TODO get response from server
    }

    fun onSignUpClick() {
        viewState.showProgress()
        checkEmailUniqueness()
        checkUsernameUniqueness()
        sendDataToServer()
        getDataFromServer()
        viewState.hideProgress()
        viewState.openMainFragment()
    }
}