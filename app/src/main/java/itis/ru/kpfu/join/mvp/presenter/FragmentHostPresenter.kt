package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.FragmentHostView
import itis.ru.kpfu.join.ui.fragment.ChatFragment
import itis.ru.kpfu.join.ui.fragment.MenuFragment
import itis.ru.kpfu.join.ui.fragment.NotificationsFragment
import itis.ru.kpfu.join.ui.fragment.ProjectsFragment
import itis.ru.kpfu.join.ui.fragment.SignInFragment

@InjectViewState
class FragmentHostPresenter(private val userRepository: UserRepository) : MvpPresenter<FragmentHostView>() {

    private val projectsFragment = ProjectsFragment.newInstance()
    private val chatFragment = ChatFragment.newInstance()
    private val notificationsFragment = NotificationsFragment.newInstance()
    private val menuFragment = MenuFragment.newInstance()

    fun onBottomNavBarClick(itemId: Int) {
        viewState.clearFragmentsStack()

        when (itemId) {
            R.id.bottom_projects -> viewState.setFragment(projectsFragment, false)
            R.id.bottom_chat -> viewState.setFragment(chatFragment, false)
            R.id.bottom_notifications -> viewState.setFragment(notificationsFragment, false)
            else -> viewState.setFragment(menuFragment, false)
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