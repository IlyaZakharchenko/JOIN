package itis.ru.kpfu.join.presentation.ui.main.projects.add

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.base.BaseView

interface AddProjectView: BaseView {
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
}