package itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo

import android.os.CountDownTimer
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.network.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.model.RegistrationFormModel
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.ConfirmEmailFormModel
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class SignUpStepTwoPresenter @Inject constructor() : BasePresenter<SignUpStepTwoView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    private var timer: CountDownTimer? = null

    private var timeLeft: Long = 15

    fun finishRegistration(user: RegistrationFormModel) {
        apiRequest
                .signUp(user)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    viewState.setSignInFragment()
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }

    fun checkButtonState(code: Observable<CharSequence>) {
        code
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState.setButtonEnabled(it.trim().length == 16) }
                .disposeWhenDestroy()
    }

    fun resendCode(email: String?) {
        apiRequest
                .confirmEmail(ConfirmEmailFormModel(email?.trim()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showWaitDialog()
                    viewState.hideKeyboard()
                }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    startCounter()
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
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