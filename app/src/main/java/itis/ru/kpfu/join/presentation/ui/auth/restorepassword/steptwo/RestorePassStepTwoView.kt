package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseView

interface RestorePassStepTwoView: BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setIsValidNewPassword(isValid: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setIsValidCode(isValid: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignInFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessMessage()
}