package itis.ru.kpfu.join.presentation.ui.auth.signup.stepone

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.network.pojo.UserRegistrationForm

@StateStrategyType(OneExecutionStateStrategy::class)
interface SignUpStepOneView: MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onFirstStepSuccess(user: UserRegistrationForm)

    fun buttonEnabled(enabled: Boolean)

    fun onPasswordsNotEquals()

    fun onInvalidUsername()

    fun onInvalidEmail()

    fun onInvalidPassword()
}