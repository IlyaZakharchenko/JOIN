package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import itis.ru.kpfu.join.api.model.ProjectMember

interface UsersView: MvpView {

    fun setUsers(users: MutableList<ProjectMember>)

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()

    fun onInviteSuccess(user: ProjectMember)
}