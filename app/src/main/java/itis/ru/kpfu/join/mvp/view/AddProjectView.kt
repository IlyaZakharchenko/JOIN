package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.api.model.Project

@StateStrategyType(OneExecutionStateStrategy::class)
interface AddProjectView: MvpView {

    fun onSaveSuccess()

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()

    fun onNameEmpty()

    fun onDescriptionEmpty()

    fun onJobsEmpty()
}