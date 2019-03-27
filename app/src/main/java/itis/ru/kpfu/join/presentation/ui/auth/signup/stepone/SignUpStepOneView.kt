package itis.ru.kpfu.join.presentation.ui.auth.signup.stepone

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.model.RegistrationFormModel
import itis.ru.kpfu.join.presentation.base.BaseView


interface SignUpStepOneView: BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignUpStepTwoFragment(user: RegistrationFormModel)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(enabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onPasswordsNotEquals()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onInvalidUsername()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onInvalidEmail()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onInvalidPassword()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun refreshErrors()
}