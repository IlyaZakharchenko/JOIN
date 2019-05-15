package itis.ru.kpfu.join.presentation.ui.main.projects.details

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.base.BaseView


interface ProjectDetailsView: BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setProject(item: ProjectModel, isMyProject: Boolean, isInProject: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRetry(errorText: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRetry()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onApplySuccess()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setUsersFragment(projectId: Long)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignInFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun exitFragment()
}