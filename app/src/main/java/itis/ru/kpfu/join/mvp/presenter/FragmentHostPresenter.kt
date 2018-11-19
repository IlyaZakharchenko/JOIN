package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.FragmentHostView
import itis.ru.kpfu.join.ui.fragment.DialogsFragment
import itis.ru.kpfu.join.ui.fragment.ProfileFragment
import itis.ru.kpfu.join.ui.fragment.NotificationsFragment
import itis.ru.kpfu.join.ui.fragment.ProjectsFragment
import itis.ru.kpfu.join.ui.fragment.SignInFragment

@InjectViewState
class FragmentHostPresenter(private val userRepository: UserRepository) : MvpPresenter<FragmentHostView>() {

    private val projectsFragment = ProjectsFragment.newInstance()
    private val dialogsFragment = DialogsFragment.newInstance()
    private val notificationsFragment = NotificationsFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    fun onBottomNavBarClick(itemId: Int) {
        viewState.clearFragmentsStack()

        when (itemId) {
            R.id.bottom_projects -> viewState.setFragment(projectsFragment, false)
            R.id.bottom_dialogs -> viewState.setFragment(dialogsFragment, false)
            R.id.bottom_notifications -> viewState.setFragment(notificationsFragment, false)
            else -> viewState.setFragment(profileFragment, false)
        }
    }

    fun checkLogin() {
        if (userRepository.getUser() != null) {
            viewState.setFragment(ProjectsFragment.newInstance(), false)
        } else {
            viewState.setFragment(SignInFragment.newInstance(), false)
        }
    }
}