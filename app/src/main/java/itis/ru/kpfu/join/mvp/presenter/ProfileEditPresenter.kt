package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.ProfileEditView

@InjectViewState
class ProfileEditPresenter(private val userRepository: UserRepository) : MvpPresenter<ProfileEditView>() {

    fun getUser() : User? {
        return userRepository.getUser()
    }

    fun changeAvatar(path: String?) {
//        var user : User? = userRepository.getUser()
//        user?.imagePath = path
//        userRepository.updateUser(user)
    }

    fun updateUser(user: User?) {
        userRepository.updateUser(user)
    }
}