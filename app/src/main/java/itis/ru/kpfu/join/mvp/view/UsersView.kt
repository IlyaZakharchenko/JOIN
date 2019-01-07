package itis.ru.kpfu.join.mvp.view

import android.support.design.circularreveal.CircularRevealHelper.Strategy
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.api.model.ProjectMember

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