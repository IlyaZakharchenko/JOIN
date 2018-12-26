package itis.ru.kpfu.join.di.component

import dagger.Subcomponent
import itis.ru.kpfu.join.di.module.PresenterModule
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.presenter.MyProjectsPresenter
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.presenter.ProfilePresenter
import itis.ru.kpfu.join.mvp.presenter.ProjectPresenter
import itis.ru.kpfu.join.mvp.presenter.ProjectsPresenter
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepOnePresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepTwoPresenter
import itis.ru.kpfu.join.mvp.presenter.UsersPresenter

@Subcomponent(modules = [PresenterModule::class])
interface PresentersComponent {

    fun provideSignInPresenter(): SignInPresenter

    fun provideFragmentHostPresenter(): FragmentHostPresenter

    fun provideSignUpStepOnePresenter(): SignUpStepOnePresenter

    fun provideSignUpStepTwoPresenter(): SignUpStepTwoPresenter

    fun provideProfilePresenter(): ProfilePresenter

    fun provideProfileEditPresenter(): ProfileEditPresenter

    fun provideProjectsPresenter(): ProjectsPresenter

    fun provideProjectPresenter(): ProjectPresenter

    fun provideMyProjectPresenter(): MyProjectsPresenter

    fun provideUsersPresenter(): UsersPresenter
}