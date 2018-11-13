package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.mvp.view.SignUpView

@InjectViewState
class SignUpPresenter: MvpPresenter<SignUpView>() {

    private fun checkUsername(username : String){
        //TODO check username uniqueness
        if (!username.isEmpty()) {

        }
        else {
            viewState.onError("Username is empty")
        }
    }

    private fun checkEmail(email : String) {
        //TODO check email uniqueness
        if (!email.isEmpty()) {

        }
        else {
            viewState.onError("Email is empty")
        }
    }

    private fun sendDataToServer() {
        //TODO send account data to server
    }

    private fun getDataFromServer() {
        //TODO get response from server
    }

    fun onSignUpClick(username : String, email : String, password : String) {
        viewState.showProgress()
        checkEmail(email)
        checkUsername(username)
        sendDataToServer()
        getDataFromServer()
        viewState.hideProgress()
        viewState.onSignUpSuccess()
    }
}