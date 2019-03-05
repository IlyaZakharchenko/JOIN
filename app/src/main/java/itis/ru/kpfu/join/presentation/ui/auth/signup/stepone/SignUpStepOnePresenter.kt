package itis.ru.kpfu.join.presentation.ui.auth.signup.stepone

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.network.pojo.UserRegistrationForm
import javax.inject.Inject

@InjectViewState
class SignUpStepOnePresenter @Inject constructor() : MvpPresenter<SignUpStepOneView>() {

    @Inject
    lateinit var api: JoinApi

    private var compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun onSignUpClick(form: UserRegistrationForm) {

        if (!hasErrors(form)) {
            compositeDisposable.add(api
                    .confirmEmail(form.email.trim())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { viewState.showProgress() }
                    .doAfterTerminate { viewState.hideProgress() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        run {
                            viewState.onFirstStepSuccess(
                                    UserRegistrationForm(username = form.username,
                                            email = form.email, password = form.password))
                        }
                    }, { viewState.onConnectionError() }))
        }
    }

    fun checkButtonState(username: Observable<CharSequence>, email: Observable<CharSequence>,
                         pass: Observable<CharSequence>, passAgain: Observable<CharSequence>) {

        compositeDisposable.add(Observable.combineLatest(username, email, pass, passAgain,
                Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean> { t1, t2, t3, t4 ->
                    t1.isNotEmpty() && t2.isNotEmpty() && t3.isNotEmpty() && t4.isNotEmpty()
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState.buttonEnabled(it) })
    }

    private fun hasErrors(form: UserRegistrationForm): Boolean {
        var hasError = false

        if (form.username.trim().length < 6) {
            viewState.onInvalidUsername()
            hasError = true
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(form.email.trim()).matches()) {
            viewState.onInvalidEmail()
            hasError = true
        }
        if (form.password.trim().length < 7) {
            viewState.onInvalidPassword()
            hasError = true
        }
        if (form.password.trim() != form.passwordAgain.trim()) {
            viewState.onPasswordsNotEquals()
            hasError = true
        }
        return hasError
    }
}