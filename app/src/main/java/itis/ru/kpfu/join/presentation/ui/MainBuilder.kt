package itis.ru.kpfu.join.presentation.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import itis.ru.kpfu.join.presentation.ui.auth.restorepassword.stepone.RestorePassStepOneFragment
import itis.ru.kpfu.join.presentation.ui.auth.restorepassword.stepone.RestorePassStepOneModule
import itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo.RestorePassStepTwoFragment
import itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo.RestorePassStepTwoModule
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import itis.ru.kpfu.join.presentation.ui.main.profile.edit.ProfileEditFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.add.AddProjectFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.my.MyProjectsFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInModule
import itis.ru.kpfu.join.presentation.ui.auth.signup.stepone.SignUpStepOneFragment
import itis.ru.kpfu.join.presentation.ui.auth.signup.stepone.SignUpStepOneModule
import itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo.SignUpStepTwoFragment
import itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo.SignUpStepTwoModule
import itis.ru.kpfu.join.presentation.ui.main.dialogs.DialogListFragment
import itis.ru.kpfu.join.presentation.ui.main.dialogs.DialogListModule
import itis.ru.kpfu.join.presentation.ui.main.dialogs.selected.ChatFragment
import itis.ru.kpfu.join.presentation.ui.main.dialogs.selected.ChatModule
import itis.ru.kpfu.join.presentation.ui.main.notifications.NotificationsFragment
import itis.ru.kpfu.join.presentation.ui.main.notifications.NotificationsModule
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileModule
import itis.ru.kpfu.join.presentation.ui.main.profile.edit.ProfileEditModule
import itis.ru.kpfu.join.presentation.ui.main.projects.add.AddProjectModule
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsModule
import itis.ru.kpfu.join.presentation.ui.main.projects.details.ProjectDetailsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.details.ProjectDetailsModule
import itis.ru.kpfu.join.presentation.ui.main.projects.edit.EditProjectFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.edit.EditProjectModule
import itis.ru.kpfu.join.presentation.ui.main.projects.my.MyProjectsModule
import itis.ru.kpfu.join.presentation.ui.main.users.UsersFragment
import itis.ru.kpfu.join.presentation.ui.main.users.UsersModule
import itis.ru.kpfu.join.presentation.ui.splash.SplashFragment
import itis.ru.kpfu.join.presentation.ui.splash.SplashModule

@Module
abstract class MainBuilder {

    @ContributesAndroidInjector(modules = [AddProjectModule::class])
    abstract fun buildAddProjectFragment(): AddProjectFragment

    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun buildChatFragment(): ChatFragment

    @ContributesAndroidInjector(modules = [DialogListModule::class])
    abstract fun buildDialogsFragment(): DialogListFragment

    @ContributesAndroidInjector(modules = [MyProjectsModule::class])
    abstract fun buildMyProjectsFragment(): MyProjectsFragment

    @ContributesAndroidInjector(modules = [NotificationsModule::class])
    abstract fun buildNotificationsFragment(): NotificationsFragment

    @ContributesAndroidInjector(modules = [ProfileEditModule::class])
    abstract fun buildProfileEditFragment(): ProfileEditFragment

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun buildProfileFragment(): ProfileFragment

    @ContributesAndroidInjector(modules = [AllProjectsModule::class])
    abstract fun buildAllProjectsFragment(): AllProjectsFragment

    @ContributesAndroidInjector(modules = [RestorePassStepOneModule::class])
    abstract fun buildRestorePassStepOneFragment(): RestorePassStepOneFragment

    @ContributesAndroidInjector(modules = [RestorePassStepTwoModule::class])
    abstract fun buildRestorePassStepTwoFragment(): RestorePassStepTwoFragment

    @ContributesAndroidInjector(modules = [SignInModule::class])
    abstract fun buildSignInFragment(): SignInFragment

    @ContributesAndroidInjector(modules = [SignUpStepOneModule::class])
    abstract fun buildSignUpStepOneFragment(): SignUpStepOneFragment

    @ContributesAndroidInjector(modules = [SignUpStepTwoModule::class])
    abstract fun buildSignUpStepTwoFragment(): SignUpStepTwoFragment

    @ContributesAndroidInjector(modules = [UsersModule::class])
    abstract fun buildUsersFragment(): UsersFragment

    @ContributesAndroidInjector(modules = [ProjectDetailsModule::class])
    abstract fun buildProjectDetailsFragment(): ProjectDetailsFragment

    @ContributesAndroidInjector(modules = [EditProjectModule::class])
    abstract fun buildEditProjectFragment(): EditProjectFragment

    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun buildSplashFragment(): SplashFragment

}