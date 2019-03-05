package itis.ru.kpfu.join.presentation.ui.main.projects.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.network.pojo.Project

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProjectDetailsView: MvpView {

    fun setProject(item: Project, isMyProject: Boolean, isInProject: Boolean)

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()

    fun onApplySuccess()
}