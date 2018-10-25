package itis.ru.kpfu.join.db.repository

import io.reactivex.Single
import itis.ru.kpfu.join.db.entity.TestEntity
import junit.framework.Test

interface TestRepository {

    fun addTests(items: List<TestEntity>)

    fun getTests(): Single<List<TestEntity>>
}