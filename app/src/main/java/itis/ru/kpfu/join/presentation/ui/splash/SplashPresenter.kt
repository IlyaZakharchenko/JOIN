package itis.ru.kpfu.join.presentation.ui.splash

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.PushModel
import itis.ru.kpfu.join.presentation.model.SignInFormModel
import itis.ru.kpfu.join.presentation.ui.FragmentHostView
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import javax.inject.Inject

@InjectViewState
class SplashPresenter @Inject constructor(var userRepository: UserRepository) : BasePresenter<SplashView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest

    private var pushModel: PushModel? = null


    @SuppressLint("CheckResult")
    fun wakeServerUp() {
        apiRequest
                .signIn(SignInFormModel("", "", ""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    wakeServerUp()
                }, {
                    checkLogin()
                })
    }


    private fun checkLogin() {
        if (pushModel == null) {
            if (userRepository.getUser() != null) {
                viewState.openAllProjectsFragment()
            } else {
                viewState.openSignInFragment()
            }
        }
    }
}