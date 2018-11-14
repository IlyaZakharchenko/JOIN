package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

interface FragmentHostView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setFragment(fragment: BaseFragment, addToBackStack: Boolean)

    fun clearFragmentsStack()
}