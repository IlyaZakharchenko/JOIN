package itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.base.BaseView

interface SignUpStepTwoView: BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignInFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessMessage()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(state: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateSendAgainMessage(text: String, isClickable: Boolean)
}