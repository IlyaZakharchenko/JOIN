package itis.ru.kpfu.join.presentation.ui

import com.arellomobile.mvp.InjectViewState
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.PushCategoriesType
import itis.ru.kpfu.join.presentation.model.PushModel
import itis.ru.kpfu.join.presentation.ui.main.dialogs.DialogListFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.my.MyProjectsFragment
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import itis.ru.kpfu.join.presentation.ui.main.notifications.NotificationsFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import javax.inject.Inject

@InjectViewState
class FragmentHostPresenter @Inject constructor(var userRepository: UserRepository) : BasePresenter<FragmentHostView>() {

    private var pushModel: PushModel? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.openSplashFragment()
    }

    fun onAllProject() {
        viewState.setFragment(AllProjectsFragment.newInstance(), false, clearStack = true)
    }

    fun onMyProjects() {
        viewState.setFragment(MyProjectsFragment.newInstance(), false, clearStack = true)
    }

    fun onDialogs() {
        viewState.setFragment(DialogListFragment.newInstance(), false, clearStack = true)
    }

    fun onNotifications() {
        viewState.setFragment(NotificationsFragment.newInstance(), false, clearStack = true)
    }

    fun onProfile() {
        viewState.setFragment(ProfileFragment.newInstance(userRepository.getUser()?.id
                ?: throw IllegalArgumentException("user id is null")), false, clearStack = true)
    }

    fun onReceivePush(pushModel: PushModel) {
        this.pushModel = pushModel

        when (pushModel.type) {
            PushCategoriesType.JOIN_TO_PROJECT_ACCEPT, PushCategoriesType.JOIN_TO_PROJECT_REFUSE,
            PushCategoriesType.JOIN_TO_PROJECT_INVITE, PushCategoriesType.ADD_TO_PROJECT_ACCEPT,
            PushCategoriesType.ADD_TO_PROJECT_REFUSE, PushCategoriesType.ADD_TO_PROJECT_INVITE,
            PushCategoriesType.REMOVE_FROM_PROJECT -> {
                viewState.setFragment(NotificationsFragment.newInstance(), false, clearStack = true)
                viewState.setNotificationsTabEnabled()
            }
            else -> {
                viewState.setFragment(ProfileFragment.newInstance(userRepository.getUser()?.id
                        ?: throw IllegalArgumentException("user id is null")), false, clearStack = true)
                viewState.setProfileTabEnabled()
            }
        }
    }
}
