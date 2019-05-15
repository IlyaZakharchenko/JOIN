package itis.ru.kpfu.join.presentation.ui.splash

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseView

interface SplashView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openAllProjectsFragment()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openSignInFragment()
}