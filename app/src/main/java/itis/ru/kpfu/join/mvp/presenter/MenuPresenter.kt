package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.MenuView

@InjectViewState
class MenuPresenter(private val userRepository: UserRepository) : MvpPresenter<MenuView>() {

    fun getUser() : User? {
        return userRepository.getUser()
    }

    //TODO FOR TESTING!!! DELETE THIS
    fun createUser() {
        userRepository.addUser(User(0, "DamirLapochka", "gayazov.damir@mail.ru","Дамир", null,
                "Гаязов", "+0-000-000-00-00"))
    }

    fun changeAvatar(path: String?) {
//        var user : User? = userRepository.getUser()
//        user?.imagePath = path
//        userRepository.updateUser(user)
    }
}