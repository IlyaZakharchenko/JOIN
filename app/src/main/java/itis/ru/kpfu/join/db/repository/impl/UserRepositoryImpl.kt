package itis.ru.kpfu.join.db.repository.impl

import io.realm.Realm
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.db.repository.base.BaseRepository

class UserRepositoryImpl : BaseRepository(), UserRepository {

    override fun updateUser(user: User?) {
        executeTransaction(Realm.Transaction {
            it.insertOrUpdate(user)
        })
    }

    override fun addUser(user: User) {
        executeTransaction(Realm.Transaction {
            user.id = nextKey(User::class.java)
            it.insertOrUpdate(user)
        })
    }

    override fun getUser(): User? {
        return realm.where(User::class.java).findFirst()
    }
}