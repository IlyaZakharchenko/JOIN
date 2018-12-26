package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import itis.ru.kpfu.join.api.model.Project

interface ProjectView: MvpView {

    fun setProject(item: Project, isMyProject: Boolean)

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()
}