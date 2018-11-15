package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.User

@StateStrategyType(OneExecutionStateStrategy::class)
interface SignUpStepOneView: MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onFirstStepSuccess(user: User)

    fun buttonEnabled(enabled: Boolean)

    fun onPasswordsNotEquals()

    fun onInvalidUsername()

    fun onInvalidEmail()

    fun onInvalidPassword()
}