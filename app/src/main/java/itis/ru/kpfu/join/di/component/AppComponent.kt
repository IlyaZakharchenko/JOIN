package itis.ru.kpfu.join.di.component

import dagger.Component
import itis.ru.kpfu.join.di.module.AppContextModule
import itis.ru.kpfu.join.di.module.PresenterModule
import itis.ru.kpfu.join.di.module.RepositoryModule
import itis.ru.kpfu.join.di.module.TestApiModule
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.presenter.MenuPresenter
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepOnePresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepTwoPresenter
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [TestApiModule::class, AppContextModule::class, RepositoryModule::class, PresenterModule::class])
interface AppComponent {

    fun inject(fragment: BaseFragment)

    fun provideSignInPresenter(): SignInPresenter

    fun provideFragmentHostPresenter(): FragmentHostPresenter

    fun provideSignUpStepOnePresenter(): SignUpStepOnePresenter

    fun provideSignUpStepTwoPresenter(): SignUpStepTwoPresenter

    fun provideMenuPresenter(): MenuPresenter

    fun provideProfileEditPresenter(): ProfileEditPresenter
}