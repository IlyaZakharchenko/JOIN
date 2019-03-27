package itis.ru.kpfu.join.presentation.ui.auth.signup.stepone

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import itis.ru.kpfu.join.data.network.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.model.RegistrationFormModel
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.ConfirmEmailFormModel
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class SignUpStepOnePresenter @Inject constructor() : BasePresenter<SignUpStepOneView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun onSignUp(form: RegistrationFormModel) {

        if (!hasErrors(form)) {
            apiRequest
                    .confirmEmail(ConfirmEmailFormModel(form.email?.trim()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        viewState.showWaitDialog()
                        viewState.hideKeyboard()
                    }
                    .doAfterTerminate { viewState.hideWaitDialog() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.setSignUpStepTwoFragment(RegistrationFormModel(
                                username = form.username,
                                email = form.email,
                                password = form.password)
                        )
                        viewState.refreshErrors()
                    }, {
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    })
                    .disposeWhenDestroy()
        }
    }

    fun checkButtonState(username: Observable<CharSequence>, email: Observable<CharSequence>,
                         pass: Observable<CharSequence>, passAgain: Observable<CharSequence>) {

        Observable.combineLatest(username, email, pass, passAgain,
                Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean> { t1, t2, t3, t4 ->
                    t1.isNotEmpty() && t2.isNotEmpty() && t3.isNotEmpty() && t4.isNotEmpty()
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState.setButtonEnabled(it) }
                .disposeWhenDestroy()
    }

    private fun hasErrors(form: RegistrationFormModel): Boolean {
        var hasError = false

        if (form.username?.trim()?.length ?: 0 < 6) {
            viewState.onInvalidUsername()
            hasError = true
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(form.email?.trim()).matches()) {
            viewState.onInvalidEmail()
            hasError = true
        }
        if (form.password?.trim()?.length ?: 0 < 7) {
            viewState.onInvalidPassword()
            hasError = true
        }
        if (form.password?.trim() != form.passwordAgain?.trim()) {
            viewState.onPasswordsNotEquals()
            hasError = true
        }
        return hasError
    }
}