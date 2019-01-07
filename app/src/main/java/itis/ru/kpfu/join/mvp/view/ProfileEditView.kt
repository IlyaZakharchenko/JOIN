package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProfileEditView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onError()

    fun onImageSetSuccess(url: String)

    fun onError(message : String)

    fun onEditSuccess()

    fun onImageDeleteSuccess()

    fun onEmptyName()

    fun onEmptySurname()

    fun onEmptyEmail()

    fun onEmptyUsername()

    fun onEmptyPhoneNumber()

    fun onEmptySpecializations()

    fun onInvalidUsername()

    fun onInvalidEmail()

    fun onInvalidPhoneNumber()
}