package itis.ru.kpfu.join.presentation.ui.main.projects.all

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.base.BaseView

interface AllProjectsView: BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRetry(errorText: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRetry()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProjects(projects: List<ProjectModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showBottomSheetDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideBottomSheetDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignInFragment()
}