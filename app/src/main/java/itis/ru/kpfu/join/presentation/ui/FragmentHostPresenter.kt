package itis.ru.kpfu.join.presentation.ui

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.repository.impl.UserRepositoryImpl
import itis.ru.kpfu.join.presentation.ui.main.dialogs.DialogsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.my.MyProjectsFragment
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import itis.ru.kpfu.join.presentation.ui.main.notifications.NotificationsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import javax.inject.Inject

@InjectViewState
class FragmentHostPresenter @Inject constructor() : MvpPresenter<FragmentHostView>() {

   /* @Inject
    lateinit var userRepository: UserRepository*/
    val userRepository = UserRepositoryImpl()

    private val projectsFragment = AllProjectsFragment.newInstance()
    private val myProjectsFragment = MyProjectsFragment.newInstance()
    private val dialogsFragment = DialogsFragment.newInstance()
    private val notificationsFragment = NotificationsFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance(userRepository.getUser()?.id)

    fun onBottomNavBarClick(itemId: Int) {
        viewState.clearFragmentsStack()

        when (itemId) {
            R.id.bottom_projects -> viewState.setFragment(projectsFragment, false)
            R.id.bottom_my_projects -> viewState.setFragment(myProjectsFragment, false)
            R.id.bottom_dialogs -> viewState.setFragment(dialogsFragment, false)
            R.id.bottom_notifications -> viewState.setFragment(notificationsFragment, false)
            else -> viewState.setFragment(profileFragment, false)
        }
    }

    fun checkLogin() {
        if (userRepository.getUser() != null) {
            viewState.setFragment(AllProjectsFragment.newInstance(), false)
        } else {
            viewState.setFragment(SignInFragment.newInstance(), false)
        }
    }
}