package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.api.model.InviteForm
import itis.ru.kpfu.join.api.model.ProjectMember
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.UsersView

@InjectViewState
class UsersPresenter(private val api: JoinApi, private val userRepository: UserRepository) :
        MvpPresenter<UsersView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getUsers() {
        compositeDisposable.add(
                api.getUsers(userRepository.getUser()?.token)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { viewState.showProgress() }
                        .doAfterTerminate { viewState.hideProgress() }
                        .subscribe({
                            viewState.setUsers(it)
                        }, {
                            viewState.onConnectionError()
                        })
        )
    }

    fun inviteUser(user: ProjectMember?, projectId: Long?) {
        compositeDisposable.add(
                api.inviteToProject(userRepository.getUser()?.token, InviteForm(user?.id, projectId))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            user?.let { it1 -> viewState.onInviteSuccess(it1) }
                        }, { viewState.onConnectionError() })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}