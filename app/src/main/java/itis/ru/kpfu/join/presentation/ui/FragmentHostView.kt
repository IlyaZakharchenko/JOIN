package itis.ru.kpfu.join.presentation.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.base.BaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface FragmentHostView : BaseView {

    fun setFragment(fragment: BaseFragment, addToBackStack: Boolean)

    fun clearFragmentsStack()
}