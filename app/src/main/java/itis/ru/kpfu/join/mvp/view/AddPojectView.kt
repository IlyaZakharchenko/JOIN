package itis.ru.kpfu.join.mvp.view

import com.arellomobile.mvp.MvpView
import itis.ru.kpfu.join.api.model.Project

interface AddPojectView: MvpView {

    fun onSaveSuccess()

    fun onConnectionError()

    fun showProgress()

    fun hideProgress()

    fun onNameEmpty()

    fun onDescriptionEmpty()

    fun onJobsEmpty()
}