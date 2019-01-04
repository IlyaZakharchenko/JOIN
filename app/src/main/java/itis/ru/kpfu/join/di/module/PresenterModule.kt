package itis.ru.kpfu.join.di.module

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.presenter.AddProjectPresenter
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.presenter.MyProjectsPresenter
import itis.ru.kpfu.join.mvp.presenter.NotificationsPresenter
import itis.ru.kpfu.join.mvp.presenter.ProfilePresenter
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.presenter.ProjectPresenter
import itis.ru.kpfu.join.mvp.presenter.ProjectsPresenter
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepOnePresenter
import itis.ru.kpfu.join.mvp.presenter.SignUpStepTwoPresenter
import itis.ru.kpfu.join.mvp.presenter.UsersPresenter

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
    fun menuPresenter(userRepository: UserRepository, api: JoinApi): ProfilePresenter {
        return ProfilePresenter(userRepository, api)
    }

    @Provides
    fun profileEditPresenter(api: JoinApi, userRepository: UserRepository): ProfileEditPresenter {
        return ProfileEditPresenter(api, userRepository)
    }

    @Provides
    fun projectsPresenter(api: JoinApi, userRepository: UserRepository): ProjectsPresenter {
        return ProjectsPresenter(api, userRepository)
    }

    @Provides
    fun myProjectsPresenter(api: JoinApi, userRepository: UserRepository): MyProjectsPresenter {
        return MyProjectsPresenter(api, userRepository)
    }

    @Provides
    fun projectPresenter(api: JoinApi, userRepository: UserRepository): ProjectPresenter {
        return ProjectPresenter(api, userRepository)
    }

    @Provides
    fun usersPresenter(api: JoinApi, userRepository: UserRepository): UsersPresenter {
        return UsersPresenter(api, userRepository)
    }

    @Provides
    fun addProjectPresenter(api: JoinApi, userRepository: UserRepository): AddProjectPresenter {
        return AddProjectPresenter(api, userRepository)
    }

    @Provides
    fun notificationsPresenter(api: JoinApi, userRepository: UserRepository): NotificationsPresenter {
        return NotificationsPresenter(api, userRepository)
    }
}