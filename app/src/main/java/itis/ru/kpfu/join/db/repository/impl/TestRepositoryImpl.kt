package itis.ru.kpfu.join.db.repository.impl

import io.reactivex.Single
import io.realm.Realm
import itis.ru.kpfu.join.db.entity.TestEntity
import itis.ru.kpfu.join.db.repository.TestRepository
import itis.ru.kpfu.join.db.repository.base.BaseRepository

class TestRepositoryImpl : BaseRepository(), TestRepository {
    override fun addTests(tests: List<TestEntity>) {
        executeTransaction(Realm.Transaction { realm ->
            tests.forEach { test -> realm.insertOrUpdate(test) }
        })
    }

    override fun getTests(): Single<List<TestEntity>> {
        return Single.just(realm.where(TestEntity::class.java).findAll())
    }
}