package itis.ru.kpfu.join.db.repository.impl

import io.realm.Realm
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.db.repository.base.BaseRepository

class UserRepositoryImpl : BaseRepository(), UserRepository {

    override fun updateUser(user: User?) {
        var savedUser = realm.where(User::class.java).findFirst()
        user?.let {
            realm.beginTransaction()
            if (!it.name.equals(savedUser?.name)) savedUser?.name = it.name
            if (!it.lastname.equals(savedUser?.lastname)) savedUser?.lastname = it.lastname
            if (!it.username.equals(savedUser?.username)) savedUser?.username = it.username
            if (!it.email.equals(savedUser?.email)) savedUser?.email = it.email
            if (!it.imagePath.equals(savedUser?.imagePath)) savedUser?.imagePath = it.imagePath
            if (!it.password.equals(savedUser?.password)) savedUser?.password = it.password
            if (!it.phoneNumber.equals(savedUser?.phoneNumber)) savedUser?.phoneNumber = it.phoneNumber
            realm.commitTransaction()
        }
    }

    override fun addUser(user: User) {
        executeTransaction(Realm.Transaction {
            it.insertOrUpdate(user)
        })
    }

    override fun getUser(): User? {
        return realm.where(User::class.java).findFirst()
    }
}