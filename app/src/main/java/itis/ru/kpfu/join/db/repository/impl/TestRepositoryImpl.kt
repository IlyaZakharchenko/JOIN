package itis.ru.kpfu.join.db.repository.impl

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmObject
import itis.ru.kpfu.join.db.entity.TestEntity
import itis.ru.kpfu.join.db.repository.TestRepository
import itis.ru.kpfu.join.db.repository.base.BaseRepository

class TestRepositoryImpl : BaseRepository(), TestRepository {

    override fun addTests(items: List<TestEntity>) {
        executeTransaction(
                Realm.Transaction { realm ->
                    items.forEach { item -> realm.insertOrUpdate(item) }
                }
        )
    }

    override fun getTests(): Single<List<TestEntity>> {
        return Single.just(realm.where(TestEntity::class.java).findAll())
    }
}