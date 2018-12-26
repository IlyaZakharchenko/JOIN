package itis.ru.kpfu.join.db.repository

import itis.ru.kpfu.join.db.entity.User

interface UserRepository {

    fun addUser(user: User)

    fun getUser(): User?

    fun updateUser(user: User?)
}