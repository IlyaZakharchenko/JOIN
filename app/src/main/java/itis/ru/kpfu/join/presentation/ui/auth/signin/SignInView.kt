package itis.ru.kpfu.join.presentation.ui.auth.signin

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface SignInView: MvpView {

    fun initClickListeners()

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun showResult(result: String)

    fun openSignUpFragment()

    fun signIn()

    fun onSignInError()
}