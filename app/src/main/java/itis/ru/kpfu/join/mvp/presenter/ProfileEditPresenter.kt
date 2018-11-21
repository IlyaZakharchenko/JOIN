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

    fun changeAvatar(path: String?) {
//        var user : User? = userRepository.getUser()
//        user?.imagePath = path
//        userRepository.updateUser(user)
    }

    fun updateUser(user: User?) {
        userRepository.updateUser(user)
        compositeDisposable.add(api
                .changeUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    if (it.code() != 200) {
                        viewState.onError("Не удалось передать данные")
                    }
                }, {
                    viewState.onConnectionError()
                }))
    }
}