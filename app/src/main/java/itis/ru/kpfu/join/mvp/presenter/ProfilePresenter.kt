package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.ProfileView

@InjectViewState
class ProfilePresenter(private val userRepository: UserRepository) : MvpPresenter<ProfileView>() {

    fun getUser() : User? {
        return userRepository.getUser()
    }

}