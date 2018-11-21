package itis.ru.kpfu.join.api

import com.google.gson.JsonObject
import io.reactivex.Single
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.model.UserRegistrationForm
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JoinApi {

    @POST("/registration")
    fun signUp(@Body user: UserRegistrationForm): Single<Response<Void>>

    @POST("/registration/email_confirmation")
    fun confirmEmail(@Body email: String): Single<Response<Void>>

    @POST("/login")
    fun signIn(@Body user: User): Single<Response<JsonObject>>

    @GET("/user/{id}")
    fun getUserInfo(@Header("Authorization") token: String?, @Path("id") id: Long?): Single<User>

    @POST("/user{id}/change")
    fun changeUser(@Body user: User?): Single<Response<Void>>
}