package itis.ru.kpfu.join.presentation.ui.main.projects.edit

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.base.BaseView
import itis.ru.kpfu.join.presentation.model.ProjectModel

interface EditProjectView: BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onSaveSuccess()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onNameEmpty()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onDescriptionEmpty()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onJobsEmpty()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateSpec(position: Int, spec: Specialization)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addSpec(spec: Specialization)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun removeSpec(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAddSpecDialog(requestCode: Int, position: Int, spec: Specialization?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSignInFragment()

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
    fun exit()
}