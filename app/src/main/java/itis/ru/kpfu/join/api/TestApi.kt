package itis.ru.kpfu.join.api

import io.reactivex.Single
import itis.ru.kpfu.join.db.entity.TestEntity
import itis.ru.kpfu.join.model.TestModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApi {

    @GET("/api/get")
    fun getData(@Query("name") name: String, @Query("num") count: Int): Single<List<TestEntity>>
}