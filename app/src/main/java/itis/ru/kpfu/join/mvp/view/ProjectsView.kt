package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.Project

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProjectsView: MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onError(message : String)

    fun setProjects(projects: List<Project>)
}