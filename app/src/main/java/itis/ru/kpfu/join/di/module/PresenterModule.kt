package itis.ru.kpfu.join.di.module

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.presenter.MenuPresenter
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepOnePresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepTwoPresenter
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    fun signInPresenter(api: JoinApi, userRepository: UserRepository): SignInPresenter {
        return SignInPresenter(api, userRepository)
    }

    @Provides
    fun fragmentHostPresenter(userRepository: UserRepository): FragmentHostPresenter {
        return FragmentHostPresenter(userRepository)
    }

    @Provides
    fun signUpStepOnePresenter(api: JoinApi): SignUpStepOnePresenter {
        return SignUpStepOnePresenter(api)
    }

    @Provides
    fun signUpStepTwoPresenter(api: JoinApi): SignUpStepTwoPresenter {
        return SignUpStepTwoPresenter(api)
    }

    @Provides
    fun menuPresenter(userRepository: UserRepository): MenuPresenter {
        return MenuPresenter(userRepository)
    }

    @Provides
    fun profileEditPresentet(userRepository: UserRepository): ProfileEditPresenter{
        return ProfileEditPresenter(userRepository)
    }
}