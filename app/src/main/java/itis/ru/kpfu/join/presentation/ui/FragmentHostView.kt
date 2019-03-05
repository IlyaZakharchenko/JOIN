package itis.ru.kpfu.join.presentation.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseFragment

@StateStrategyType(OneExecutionStateStrategy::class)
interface FragmentHostView : MvpView {

    fun setFragment(fragment: BaseFragment, addToBackStack: Boolean)

    fun clearFragmentsStack()
}