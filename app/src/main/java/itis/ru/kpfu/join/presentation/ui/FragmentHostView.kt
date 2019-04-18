package itis.ru.kpfu.join.presentation.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.base.BaseView

interface FragmentHostView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setFragment(fragment: BaseFragment, addToBackStack: Boolean, clearStack: Boolean = false)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setAllProjectsTabEnabled()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setMyProjectsTabEnabled()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNotificationsTabEnabled()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProfileTabEnabled()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDialogsTabEnabled()
}