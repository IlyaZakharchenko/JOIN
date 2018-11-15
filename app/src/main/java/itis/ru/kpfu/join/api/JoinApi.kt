package itis.ru.kpfu.join.api

import io.reactivex.Single
import itis.ru.kpfu.join.db.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface JoinApi {

    @POST("/registration")
    fun signUp(@Body user: User): Single<Response<Void>>

    @POST("/registration/email_confirmation")
    fun confirmEmail(@Body email: String): Single<Response<Void>>

    @POST("/account/login")
    fun signIn(@Body user: User): Single<Response<Void>>

}