package itis.ru.kpfu.join.presentation.ui

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.db.repository.impl.UserRepositoryImpl
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.ui.main.dialogs.DialogsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.my.MyProjectsFragment
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import itis.ru.kpfu.join.presentation.ui.main.notifications.NotificationsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import javax.inject.Inject

@InjectViewState
class FragmentHostPresenter @Inject constructor(var userRepository: UserRepository) : BasePresenter<FragmentHostView>() {

    private val projectsFragment = AllProjectsFragment.newInstance()
    private val myProjectsFragment = MyProjectsFragment.newInstance()
    private val dialogsFragment = DialogsFragment.newInstance()
    private val notificationsFragment = NotificationsFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance(userRepository.getUser()?.id ?: -1)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        checkLogin()
    }

    fun onAllProject() {
        viewState.setFragment(projectsFragment, false, true)
    }

    fun onMyProjects() {
        viewState.setFragment(myProjectsFragment, false, true)
    }

    fun onDialogs() {
        viewState.setFragment(dialogsFragment, false, true)
    }

    fun onNotifications() {
        viewState.setFragment(notificationsFragment, false, true)
    }

    fun onProfile() {
        viewState.setFragment(profileFragment, false, true)
    }

    private fun checkLogin() {
        if (userRepository.getUser() != null) {
            viewState.setFragment(AllProjectsFragment.newInstance(), false)
        } else {
            viewState.setFragment(SignInFragment.newInstance(), false)
        }
    }
}