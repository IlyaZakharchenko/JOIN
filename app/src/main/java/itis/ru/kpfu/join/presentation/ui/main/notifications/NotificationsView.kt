package itis.ru.kpfu.join.presentation.ui.main.notifications

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.network.pojo.Notification

@StateStrategyType(OneExecutionStateStrategy::class)
interface NotificationsView: MvpView {

    fun setNotifications(notifications: List<Notification>)

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()

    fun onDeleteSuccess(position: Int)
}