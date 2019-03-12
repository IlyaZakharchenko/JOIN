package itis.ru.kpfu.join.presentation.ui.main.projects.details

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.network.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.model.InviteFormModel
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class ProjectDetailsPresenter @Inject constructor() : BasePresenter<ProjectDetailsView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun getProject(projectId: Long) {
        apiRequest.getProject(userRepository.getUser()?.token, projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setProject(it, (it.leader?.id == userRepository.getUser()?.id), it
                            .participants?.map { it1 -> it1.id }?.contains(userRepository.getUser()?.id)
                            ?: false)

                }, {
                    viewState.showRetry(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }

    fun onRetry(projectId: Long) {
        getProject(projectId)
    }

    fun sendApply(projectId: Long?) {
        apiRequest
                .joinProject(userRepository.getUser()?.token,
                        InviteFormModel(userRepository.getUser()?.id, projectId))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    viewState.onApplySuccess()
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }
}