package itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo

import android.os.CountDownTimer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.network.pojo.UserRegistrationForm
import javax.inject.Inject

@InjectViewState
class SignUpStepTwoPresenter @Inject constructor() : MvpPresenter<SignUpStepTwoView>() {

    @Inject
    lateinit var api: JoinApi

    private val compositeDisposable = CompositeDisposable()

    private var timer: CountDownTimer? = null

    private var timeLeft: Long = 15

    fun finishRegistration(user: UserRegistrationForm) {
        compositeDisposable.add(api
                .signUp(user)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe(
                        {
                            run {
                                if (it.code() == 200) {
                                    viewState.onRegistrationSuccess()
                                } else {
                                    viewState.onCodeInvalid()
                                }
                            }
                        }, { viewState.onConnectionError() })
        )
    }

    fun checkButtonState(code: Observable<CharSequence>) {
        compositeDisposable.add(code
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState.buttonEnabled(it.trim().length == 16) })
    }

    fun resendCode(email: String) {
        compositeDisposable.add(api
                .confirmEmail(email.trim())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    run {
                        startCounter()
                    }
                }, { viewState.onConnectionError() }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun startCounter() {
        runTimer(15000)
    }

    fun stopTimer() {
        timer?.cancel()
    }

    fun resumeTimer() {
        runTimer(timeLeft * 1000)
    }

    private fun runTimer(time: Long) {
        timer?.cancel()
        timer = null
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished / 1000
                viewState.updateSendAgainMessage(
                        "Повторная отправка кода возможна через ${timeLeft} секунд", false)
            }

            override fun onFinish() {
                viewState.updateSendAgainMessage("Выслать код еще раз", true)
            }
        }
        timer?.start()
    }
}