package itis.ru.kpfu.join.di.component

import dagger.Component
import itis.ru.kpfu.join.di.module.AppContextModule
import itis.ru.kpfu.join.di.module.PresenterModule
import itis.ru.kpfu.join.di.module.RepositoryModule
import itis.ru.kpfu.join.di.module.JoinApiModule
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.presenter.ProfilePresenter
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepOnePresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepTwoPresenter
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [JoinApiModule::class, AppContextModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(fragment: BaseFragment)

    fun providePresenters(): PresentersComponent
}