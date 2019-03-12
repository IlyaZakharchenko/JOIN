package itis.ru.kpfu.join.presentation.ui.main.profile.edit

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.base.BaseView

interface ProfileEditView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onError()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateSpec(position: Int, spec: Specialization)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addSpec(spec: Specialization)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun removeSpec(position: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAddSpecDialog(requestCode: Int, position: Int, spec: Specialization?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showChooseImageDialog(requestCode: Int, imagesLimit: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onImageSetSuccess(url: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onError(message : String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEditSuccess()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onImageDeleteSuccess()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEmptyName()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEmptySurname()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEmptyEmail()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEmptyUsername()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEmptyPhoneNumber()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onEmptySpecializations()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onInvalidUsername()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onInvalidEmail()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onInvalidPhoneNumber()
}