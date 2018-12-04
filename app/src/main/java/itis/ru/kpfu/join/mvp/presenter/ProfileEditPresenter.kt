package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.ProfileEditView

@InjectViewState
class ProfileEditPresenter(private val api: JoinApi, private val userRepository: UserRepository) :
        MvpPresenter<ProfileEditView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getUser(): User? {
        return userRepository.getUser()
    }

    fun updateUser(user: User?) {

        if (!hasErrors(user)) {
            compositeDisposable.add(api
                    .changeUser(userRepository.getUser()?.token, user, userRepository.getUser()?.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { viewState.showProgress() }
                    .doAfterTerminate { viewState.hideProgress() }
                    .subscribe({
                        if (it.code() == 200) {
                            userRepository.updateUser(user)
                            viewState.onEditSuccess()
                        } else {
                            viewState.onError("Не удалось передать данные")
                        }
                    }, {
                        it.printStackTrace()
                        viewState.onConnectionError()
                    }))
        }
    }

    private fun hasErrors(user: User?): Boolean {
        var hasError = false

        if (user?.name.isNullOrEmpty()) {
            viewState.onEmptyName()
            hasError = true
        }
        if (user?.lastname.isNullOrEmpty()) {
            viewState.onEmptySurname()
            hasError = true
        }
        if (user?.email.isNullOrEmpty()) {
            viewState.onEmptyEmail()
            hasError = true
        }
        if (user?.username.isNullOrEmpty()) {
            viewState.onEmptyUsername()
            hasError = true
        }
        if (user?.phoneNumber.isNullOrEmpty()) {
            viewState.onEmptyPhoneNumber()
            hasError = true
        }
        if (user?.phoneNumber?.trim()?.length != 10) {
            viewState.onInvalidPhoneNumber()
            hasError = true
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user?.email?.trim()).matches()) {
            viewState.onInvalidEmail()
            hasError = true
        }
        if (user?.username?.trim()?.length ?: 0 < 6) {
            viewState.onInvalidUsername()
            hasError = true
        }
        if (user?.specializations?.size == 0) {
            viewState.onEmptySpecializations()
            hasError = true
        }

        return hasError
    }
}