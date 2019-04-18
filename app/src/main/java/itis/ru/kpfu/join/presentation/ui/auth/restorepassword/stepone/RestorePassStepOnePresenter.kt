package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.stepone

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class RestorePassStepOnePresenter @Inject constructor() : BasePresenter<RestorePassStepOneView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun onSendCode(email: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            viewState.setIsValidEmail(false)
            return
        } else {
            apiRequest
                    .restorePassChange(email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { viewState.showWaitDialog() }
                    .doAfterTerminate {
                        viewState.hideWaitDialog()
                        viewState.setIsValidEmail(true)
                    }
                    .subscribe({
                        viewState.setStepTwoFragment(email)
                    }, {
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    })
                    .disposeWhenDestroy()
        }
    }

}