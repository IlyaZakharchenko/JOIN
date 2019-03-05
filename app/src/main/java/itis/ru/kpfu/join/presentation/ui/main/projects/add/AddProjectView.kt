package itis.ru.kpfu.join.presentation.ui.main.projects.add

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

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