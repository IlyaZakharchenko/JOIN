package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import itis.ru.kpfu.join.model.ProjectMember

interface UsersView: MvpView {

    fun setUsers(users: List<ProjectMember>)

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()
}