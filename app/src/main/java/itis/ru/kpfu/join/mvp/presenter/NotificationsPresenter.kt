package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.api.model.Notification
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.NotificationsView

@InjectViewState
class NotificationsPresenter(private val api: JoinApi, private val userRepository: UserRepository) :
        MvpPresenter<NotificationsView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getNotifications() {
        compositeDisposable.add(
                api
                        .getNotifications(userRepository.getUser()?.token, userRepository.getUser()?.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { viewState.showProgress() }
                        .doAfterTerminate { viewState.hideProgress() }
                        .subscribe({
                            viewState.setNotifications(it)
                        },{ viewState.onConnectionError() })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}