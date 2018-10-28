package itis.ru.kpfu.join.db.repository

import io.reactivex.Single
import itis.ru.kpfu.join.db.entity.User

interface UserRepository {

    fun addUser(user: User)

    fun getUser(): User?
}