package itis.ru.kpfu.join.presentation.ui.main.notifications

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.model.NotificationModel
import itis.ru.kpfu.join.presentation.base.BaseView

interface NotificationsView: BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNotifications(notifications: List<NotificationModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRetry(errorText: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRetry()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDeleteSuccess(position: Int)
}