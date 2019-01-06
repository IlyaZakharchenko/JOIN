package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import itis.ru.kpfu.join.db.entity.User

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProfileView: MvpView {

    fun onImageSetSuccess(url: String)

    fun onConnectionError()

    fun onError(message: String)

    fun onImageDeleteSuccess()

    fun showProgress()

    fun hideProgress()

    fun initFields(user: User)
}