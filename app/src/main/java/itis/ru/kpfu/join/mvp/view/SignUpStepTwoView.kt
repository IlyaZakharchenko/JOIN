package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface SignUpStepTwoView: MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onCodeInvalid()

    fun onRegistrationSuccess()

    fun buttonEnabled(state: Boolean)

    fun updateSendAgainMessage(text: String, isClickable: Boolean)
}