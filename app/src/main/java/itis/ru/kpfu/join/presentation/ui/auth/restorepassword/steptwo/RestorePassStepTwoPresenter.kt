package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.RestorePassFormModel
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class RestorePassStepTwoPresenter @Inject constructor() : BasePresenter<RestorePassStepTwoView>() {

    @Inject
    lateinit var email: String
    @Inject
    lateinit var joinApiRequest: JoinApiRequest
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun onPasswordChange(newPass: String, code: String) {
        if (newPass.length < 6) {
            viewState.setIsValidNewPassword(false)
            return
        } else if (code.length != 6) {
            viewState.setIsValidCode(false)
            return
        } else {
            viewState.setIsValidCode(true)
            viewState.setIsValidNewPassword(true)

            joinApiRequest
                    .restorePassChange(RestorePassFormModel(email, newPass, code))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { viewState.showWaitDialog() }
                    .doAfterTerminate { viewState.hideWaitDialog() }
                    .subscribe({
                        viewState.showSuccessMessage()
                        viewState.setSignInFragment()
                    }, {
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    })
        }
    }
}