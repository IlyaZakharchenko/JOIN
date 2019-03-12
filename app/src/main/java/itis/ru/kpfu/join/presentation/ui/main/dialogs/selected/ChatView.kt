package itis.ru.kpfu.join.presentation.ui.main.dialogs.selected

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface ChatView: BaseView {
}