package itis.ru.kpfu.join.presentation.ui.auth.signin

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseView

interface SignInView : BaseView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignUpFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAllProjectsFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setRestorePasswordFragment()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setEmailErrorEnabled(enabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setPasswordErrorEnabled(enabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setBottomNavBarFirstPageEnabled()
}