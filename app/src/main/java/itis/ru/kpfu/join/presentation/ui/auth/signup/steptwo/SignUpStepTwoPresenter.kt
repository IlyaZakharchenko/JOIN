package itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo

import android.os.CountDownTimer
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
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
    @Inject
    lateinit var userRegistrationFormModel: RegistrationFormModel

    private var timer: CountDownTimer? = null

    private var timeLeft: Long = 15

    override fun attachView(view: SignUpStepTwoView?) {
        super.attachView(view)
        resumeTimer()
    }

    override fun detachView(view: SignUpStepTwoView) {
        super.detachView(view)
        stopTimer()

    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startTimer()
    }

    fun onRegistrationFinish(code: String) {
        apiRequest
                .signUp(userRegistrationFormModel.also { it.code = code })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    viewState.showSuccessMessage()
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

    fun onResendCode() {
        apiRequest
                .confirmEmail(ConfirmEmailFormModel(userRegistrationFormModel.email?.trim()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showWaitDialog()
                    viewState.hideKeyboard()
                }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    startTimer()
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                })
                .disposeWhenDestroy()
    }

    private fun startTimer() {
        runTimer(15000)
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun resumeTimer() {
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