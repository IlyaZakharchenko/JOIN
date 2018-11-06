package itis.ru.kpfu.join.di.module

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.api.TestApi
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun signInPresenter(api: TestApi, userRepository: UserRepository): SignInPresenter {
        return SignInPresenter(api, userRepository)
    }

    @Provides
    @Singleton
    fun fragmentHostPresenter(userRepository: UserRepository): FragmentHostPresenter {
        return FragmentHostPresenter(userRepository)
    }
}