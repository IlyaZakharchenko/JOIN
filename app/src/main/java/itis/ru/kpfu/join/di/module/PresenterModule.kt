package itis.ru.kpfu.join.di.module

import com.google.android.gms.signin.SignIn
import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.api.TestApi
import itis.ru.kpfu.join.db.repository.TestRepository
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun signInPresenter(api: TestApi, userRepository: UserRepository): SignInPresenter {
        return SignInPresenter(api, userRepository)
    }



}