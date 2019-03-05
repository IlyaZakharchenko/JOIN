package itis.ru.kpfu.join.presentation.ui.main.notifications

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.network.pojo.NotificationResponse
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.ui.main.notifications.NotificationsView
import javax.inject.Inject

@InjectViewState
class NotificationsPresenter @Inject constructor() : MvpPresenter<NotificationsView>() {

    @Inject
    lateinit var api: JoinApi
    @Inject
    lateinit var userRepository: UserRepository

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
                        }, { viewState.onConnectionError() })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun responseToNotification(id: Long, response: NotificationResponse) {
        compositeDisposable.add(
                api
                        .responseToNotification(userRepository.getUser()?.token, response, id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { viewState.showProgress() }
                        .doAfterTerminate { viewState.hideProgress() }
                        .subscribe({
                            if (it.isSuccessful)
                                getNotifications()
                            else
                                viewState.onConnectionError()
                        }, {
                            viewState.onConnectionError()
                        })
        )
    }

    fun removeNotification(id: Long?, position: Int) {
        compositeDisposable.add(
                api
                        .deleteNotification(userRepository.getUser()?.token, id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isSuccessful)
                                viewState.onDeleteSuccess(position)
                            else
                                viewState.onConnectionError()
                        }, {
                            viewState.onConnectionError()
                        })
        )
    }
}