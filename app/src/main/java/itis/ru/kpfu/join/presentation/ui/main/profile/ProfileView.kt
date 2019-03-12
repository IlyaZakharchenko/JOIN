package itis.ru.kpfu.join.presentation.ui.main.profile

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.presentation.base.BaseView

interface ProfileView: BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setChangedPhotoProfile(url: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRetry(errorText: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRetry()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun initFields(user: User)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showChooseImageDialog(requestCode: Int, limit: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideCollapsingToolbar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCollapsingToolbar()
}