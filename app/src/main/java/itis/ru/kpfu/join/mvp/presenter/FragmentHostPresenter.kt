package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.view.FragmentHostView
import itis.ru.kpfu.join.ui.fragment.ChatFragment
import itis.ru.kpfu.join.ui.fragment.MenuFragment
import itis.ru.kpfu.join.ui.fragment.NotificationsFragment
import itis.ru.kpfu.join.ui.fragment.ProjectsFragment

@InjectViewState
class FragmentHostPresenter : MvpPresenter<FragmentHostView>() {

    private val projectsFragment = ProjectsFragment.newInstance()
    private val chatFragment = ChatFragment.newInstance()
    private val notificationsFragment = NotificationsFragment.newInstance()
    private val menuFragment = MenuFragment.newInstance()

    fun onBottomNavBarClick(itemId: Int) {
        when (itemId) {
            R.id.bottom_projects -> viewState.setFragment(projectsFragment, true)
            R.id.bottom_chat -> viewState.setFragment(chatFragment, true)
            R.id.bottom_notifications -> viewState.setFragment(notificationsFragment, true)
            else -> viewState.setFragment(menuFragment, true)
        }
    }
}