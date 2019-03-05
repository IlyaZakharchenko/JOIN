package itis.ru.kpfu.join.presentation.ui.main.users

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.network.pojo.ProjectMember

@StateStrategyType(OneExecutionStateStrategy::class)
interface UsersView: MvpView {

    fun setUsers(users: MutableList<ProjectMember>)

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()

    fun onInviteSuccess(user: ProjectMember)

    fun hideInnerProgress()

    fun showInnerProgress()
}