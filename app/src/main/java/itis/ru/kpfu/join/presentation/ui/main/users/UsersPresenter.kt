package itis.ru.kpfu.join.presentation.ui.main.users

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.model.InviteFormModel
import itis.ru.kpfu.join.presentation.model.ProjectMemberModel
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class UsersPresenter @Inject constructor() : BasePresenter<UsersView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun getUsers(projectId: Long?) {
        apiRequest.getUsers(userRepository.getUser()?.token, projectId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setUsers(it)
                }, {
                    viewState.showRetry(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }

    fun onRetry(projectId: Long?) {
        getUsers(projectId)
    }

    fun searchUsers(projectId: Long?, username: String?, specName: String? = null, exp: String? = null,
                    lvl: String? = null) {

        val spec = if (specName == "Ничего не выбрано") null else specName
        val experience = if (exp == "Ничего не выбрано") null else exp
        var level = if (lvl == "Ничего не выбрано") null else lvl

        when (level) {
            "Junior" -> level = "0"
            "Middle" -> level = "1"
            "Senior" -> level = "2"
        }

        apiRequest.getUsers(userRepository.getUser()?.token, projectId, username, spec, level, experience)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setUsers(it)
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }

    fun inviteUser(user: ProjectMemberModel?, projectId: Long?) {
        apiRequest.inviteToProject(userRepository.getUser()?.token, InviteFormModel(user?.id, projectId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    user?.let { it1 -> viewState.onInviteSuccess(it1) }
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }
}