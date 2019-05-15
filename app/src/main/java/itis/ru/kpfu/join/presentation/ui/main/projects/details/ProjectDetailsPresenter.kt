package itis.ru.kpfu.join.presentation.ui.main.projects.details

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.data.network.joinapi.pojo.ExcludeRequest
import itis.ru.kpfu.join.data.network.joinapi.pojo.ExitRequest
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
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
    @Inject
    @JvmField
    var projectId: Long = -1L

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getProject()
    }

    private fun getProject() {
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
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showRetry(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()
    }

    fun onRetry() {
        getProject()
    }

    fun onAddUser() {
        viewState.setUsersFragment(projectId)
    }

    fun onSendApply() {
        apiRequest
                .joinProject(userRepository.getUser()?.token,
                        InviteFormModel(userRepository.getUser()?.id, projectId))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    viewState.onApplySuccess()
                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()
    }

    fun onUserExclude(excludeUserId: Long) {
        apiRequest
                .excludeFromProject(
                        userRepository.getUser()?.token,
                        excludeUserId,
                        ExcludeRequest(excludeUserId, projectId)
                )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    getProject()
                }, {
                    viewState.showErrorDialog("Ошибка при исключении пользователя")
                })
                .disposeWhenDestroy()
    }

    fun onExit() {
        apiRequest
                .exitFromProject(
                        userRepository.getUser()?.token,
                        userRepository.getUser()?.id,
                        ExitRequest(userRepository.getUser()?.id, projectId)
                )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .subscribe({
                    viewState.hideWaitDialog()
                    viewState.exitFragment()
                }, {
                    viewState.hideWaitDialog()
                    viewState.showErrorDialog("Ошибка при выходе из проекта")
                }).disposeWhenDestroy()
    }
}