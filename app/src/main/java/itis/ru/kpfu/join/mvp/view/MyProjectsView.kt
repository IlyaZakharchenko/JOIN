package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.api.model.Project

@StateStrategyType(OneExecutionStateStrategy::class)
interface MyProjectsView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun onConnectionError()

    fun onError(message : String)

    fun setProjects(projects: List<Project>)
}