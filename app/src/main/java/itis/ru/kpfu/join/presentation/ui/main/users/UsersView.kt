package itis.ru.kpfu.join.presentation.ui.main.users

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.model.ProjectMemberModel
import itis.ru.kpfu.join.presentation.base.BaseView

interface UsersView: BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setUsers(users: MutableList<ProjectMemberModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onInviteSuccess(user: ProjectMemberModel)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRetry(errorText: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRetry()
}