package itis.ru.kpfu.join.presentation.ui.main.notifications

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.data.network.joinapi.pojo.NotificationResponse
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class NotificationsPresenter @Inject constructor() : BasePresenter<NotificationsView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    private val compositeDisposable = CompositeDisposable()

    fun onRetry() {
        getNotifications()
    }

    fun getNotifications() {
        apiRequest
                .getNotifications(userRepository.getUser()?.token, userRepository.getUser()?.id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setNotifications(it)
                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showRetry(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun responseToNotification(id: Long, response: NotificationResponse) {
        apiRequest
                .responseToNotification(userRepository.getUser()?.token, response, id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    getNotifications()
                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()
    }
}