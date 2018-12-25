package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.ProjectView

@InjectViewState
class ProjectPresenter(private val api: JoinApi, private val userRepository: UserRepository): MvpPresenter<ProjectView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getProject(id: Long){
        compositeDisposable.add(
                api.getProject(userRepository.getUser()?.token, id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { viewState.showProgress() }
                        .doAfterTerminate { viewState.hideProgress() }
                        .subscribe({
                            viewState.setProject(it)
                        },{
                            viewState.onConnectionError()
                        })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}