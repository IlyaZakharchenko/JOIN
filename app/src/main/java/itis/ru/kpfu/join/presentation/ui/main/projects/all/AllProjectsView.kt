package itis.ru.kpfu.join.presentation.ui.main.projects.all

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.network.pojo.Project

@StateStrategyType(OneExecutionStateStrategy::class)
interface AllProjectsView: MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onError(message : String)

    fun setProjects(projects: List<Project>)

    fun hideInnerProgress()

    fun showInnerProgress()
}