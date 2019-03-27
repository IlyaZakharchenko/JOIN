package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.stepone

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface RestorePassStepOneView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setIsValidEmail(isValid: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setStepTwoFragment(email: String)
}