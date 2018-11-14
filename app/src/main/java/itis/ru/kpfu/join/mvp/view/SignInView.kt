package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SignInView: MvpView {

    fun initClickListeners()

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun showResult(result: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openSignUpFragment()

    fun signIn()

    fun onSignInError()
}