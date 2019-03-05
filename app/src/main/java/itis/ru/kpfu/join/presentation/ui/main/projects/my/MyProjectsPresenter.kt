package itis.ru.kpfu.join.presentation.ui.main.projects.my

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.db.repository.UserRepository
import javax.inject.Inject

@InjectViewState
class MyProjectsPresenter @Inject constructor() : MvpPresenter<MyProjectsView>() {

    @Inject
    lateinit var api: JoinApi
    @Inject
    lateinit var userRepository: UserRepository

    private val compositeDisposable = CompositeDisposable()

    fun getProjects() {
        compositeDisposable.add(
                api.getMyProjects(userRepository.getUser()?.token, userRepository.getUser()?.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { viewState.showProgress() }
                        .doAfterTerminate { viewState.hideProgress() }
                        .subscribe({ viewState.setProjects(it) }, { viewState.onConnectionError() })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}