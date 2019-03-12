package itis.ru.kpfu.join.presentation.ui.main.projects.my

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.network.request.JoinApiRequest
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class MyProjectsPresenter @Inject constructor() : BasePresenter<MyProjectsView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun onRetry() {
        getProjects()
    }

    fun getProjects() {
        apiRequest.getMyProjects(userRepository.getUser()?.token, userRepository.getUser()?.id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setProjects(it)
                }, {
                    viewState.showRetry(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }
}