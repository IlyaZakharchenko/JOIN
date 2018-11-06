package itis.ru.kpfu.join.db.repository

import io.reactivex.Single
import itis.ru.kpfu.join.db.entity.TestEntity

interface TestRepository {

    fun addTests(tests: List<TestEntity>)

    fun getTests(): Single<List<TestEntity>>
}